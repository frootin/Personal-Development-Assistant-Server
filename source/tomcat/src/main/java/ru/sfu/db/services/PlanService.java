package ru.sfu.db.services;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.PlanRepository;
import ru.sfu.db.repositories.TaskPlanRepository;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.PlanDto;
import ru.sfu.objects.TaskDto;
import ru.sfu.util.JsonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

//@Transactional
//@AllArgsConstructor
@Service
public class PlanService {
    @PersistenceContext
    private EntityManager entityManager;
    private PlanRepository repository;
    private TaskPlanRepository taskPlanRepository;

    @Autowired
    public PlanService(PlanRepository repository, TaskPlanRepository taskPlanRepository) {
        this.repository = repository;
        this.taskPlanRepository = taskPlanRepository;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public Plan save(Plan plan) {
        return repository.save(plan);
    }

    public Plan findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.delete(findById(id));
    }

    public List<Plan> getPlansForUser(User user) {
        /**Session session = getSession();
        CriteriaQuery<Plan> cq = session.getCriteriaBuilder().createQuery(Plan.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Plan> plan = cq.from(Plan.class);
        Predicate finalPredicate = criteriaBuilder.equal(plan.get("userId"), user);
        return session.createQuery(cq.where(finalPredicate)).getResultList();*/
        return repository.findPlansByUserId(user);
    }

    public List<Plan> getFullPlansForUser(User user) {
        return repository.findFullPlansByUserId(user);
    }

    public List<Plan> getFullPlansForUserByStatus(User user, int status) {
        return repository.findFullPlansByUserIdAndStatus(user, status);
    }

    public long getNumberOfTasksInPLan(Plan plan) {
        return taskPlanRepository.countByPlan(plan);
    }

    public PlanDto getFullDtoFromPlan(Plan plan, TaskService taskService) {
        List<Task> tasks = taskService.getTasksForPlan(plan);
        List<Category> categories = tasks.stream().map(Task::getCategoryId).distinct().toList();
        int overallSum = tasks.stream().mapToInt(item -> item.getEstimate()).sum();
        int doneSum = tasks.stream().filter(c -> c.getStatus() == Task.DONE_STATUS).mapToInt(item -> item.getEstimate()).sum();
        PlanDto planDto = JsonUtil.ModelToDto(plan, PlanDto.class);
        planDto.setCategories(JsonUtil.mapList(categories, CategoryDto.class));
        planDto.setGoalPoints(overallSum);
        planDto.setDonePoints(doneSum);
        planDto.setTasks(JsonUtil.mapList(tasks, TaskDto.class));
        return planDto;
    }
}
