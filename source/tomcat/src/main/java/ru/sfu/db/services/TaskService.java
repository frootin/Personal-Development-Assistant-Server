package ru.sfu.db.services;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.db.models.*;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.db.repositories.TaskPlanRepository;
import ru.sfu.db.repositories.TaskRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.sfu.exceptions.*;

@AllArgsConstructor
@Service
public class TaskService {
    @PersistenceContext
    private EntityManager entityManager;
    private TaskRepository repository;
    private TaskPlanRepository taskPlanRepository;
    private CategoryRepository categoryRepository;

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public TaskService(TaskRepository repository, TaskPlanRepository taskPlanRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.taskPlanRepository = taskPlanRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task save(Task task) {
        return repository.save(task);
    }

    public Task findById(long id) throws NoSuchTaskException {
        return repository.findById(id).orElseThrow(NoSuchTaskException::new);
    }

    public void delete(long id) throws NoSuchTaskException {
        repository.delete(findById(id));
    }

    public List<Task> getTasksForUser(User user) {
        return repository.findTaskByUserId(user);
    }

    public List<Task> getFixedTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndStartDateOrStopDate(user, date, date);
    }

    public List<Task> getDoneTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndDoneByBetweenAndStartDateIsNot(user, date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX), date);
    }

    public List<Task> getLateTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndDoneByIsNullAndStopDateLessThan(user, date);
    }

    public List<Task> getFreeTasks(User user) {
        return repository.findTaskByUserIdAndStartDateIsNullAndStopDateIsNull(user);
    }

    public List<Task> getTasksOnDeadline(User user, LocalDate date) {
        return repository.findTaskByUserIdAndStartDateIsLessThanAndStopDateIsGreaterThan(user, date, date);
    }

    public void addTaskToPlan(Task task, Plan plan, long step) {
        TaskPlan taskPlan = new TaskPlan(new TaskPlanId(task.getId(), plan.getId()), task, plan, (int) step);
        taskPlanRepository.save(taskPlan);
    }

    @Transactional
    public void addTaskToPlan(Task task, Plan plan) {
        long step = taskPlanRepository.countByPlan(plan) + 1;
        TaskPlan taskPlan = new TaskPlan(new TaskPlanId(task.getId(), plan.getId()), task, plan, (int) step);
        taskPlanRepository.save(taskPlan);
    }

    public List<Task> getTasksInCategoryBetweenDates(Category category, LocalDate startDate, LocalDate endDate) {
        return repository.findTaskByCategoryIdAndDoneByBetween(category, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public List<Task> getTasksBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return repository.findTaskByUserIdAndDoneByBetween(user, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX));
    }

    public Map<Category, List<Task>> getTasksBetweenDatesInActive(User user, LocalDate startDate, LocalDate endDate) {
        List<Category> categories = categoryRepository.findCategoriesByUserIdAndOnWatchIsTrue(user);
        Map<Category, List<Task>> categoryListMap = new HashMap<>();
        for (Category cat: categories) {
            categoryListMap.put(cat, new ArrayList<Task>());
        }
        List<Task> tasks = repository.findTaskByUserIdAndDoneByBetweenAndInCategories(user, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), categories);
        Map<Category, List<Task>> tasksByCategory = tasks.stream().collect(Collectors.groupingBy(item -> item.getCategoryId()));
        tasksByCategory.forEach((key, value) -> categoryListMap.merge(key, value, (v1, v2) -> v2));
        return categoryListMap;
    }

    public List<Task> filterByFields(User user, String name, String details, Integer status) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<Task> cq = session.getCriteriaBuilder().createQuery(Task.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Task> task = cq.from(Task.class);
        predicates.add(criteriaBuilder.equal(task.get("userId"), user));

        if (name != null) {
            predicates.add(criteriaBuilder.like(task.get("name"), "%" + name + "%"));
        }

        if (details != null) {
            predicates.add(criteriaBuilder.like(task.get("details"), "%" + details + "%"));
        }

        if (status != null) {
            predicates.add(criteriaBuilder.equal(task.get("status"), status));
        }
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }
}
