package ru.sfu.db.services;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.sfu.exceptions.*;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.objects.StorageSearchDto;

@AllArgsConstructor
//@Scope(proxyMode = ScopedProxyMode.INTERFACES)
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
    public TaskService(TaskRepository repository,
                       TaskPlanRepository taskPlanRepository,
                       CategoryRepository categoryRepository) {
        this.repository = repository;
        this.taskPlanRepository = taskPlanRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task save(Task task) {
        if (task.getStartDate() != null & task.getStopDate() != null) {
            if (task.getStartDate().isAfter(task.getStopDate())) {
                return null;
            }
            if (task.getStartDate().equals(task.getStopDate()) &
                    task.getStartTime() != null &
                    task.getStopTime() != null) {
                if (task.getStartTime().isAfter(task.getStopTime())) return null;
            }
        }
        if (task.getEstimate() > 100) return null;
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

    public List<Task> getTasksForPlan(Plan plan) {
        return repository.findTaskByPlan(plan);
    }

    public List<Task> getFixedTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndStartDateOrStopDate(user, date, date);
    }

    public List<Task> getDoneTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndDoneByTmzBetweenAndDateIsNot(
                user,
                date.atStartOfDay(),
                LocalDateTime.of(date, LocalTime.MAX), date);
    }

    public List<Task> getLateTasksForDate(User user, LocalDate date) {
        return repository.findTaskByUserIdAndStatusAndStopDateLessThan(user, Task.NOT_DONE_STATUS, date);
    }

    public List<Task> getFreeTasks(User user) {
        return repository.findTaskByUserIdAndStopDateIsNullAndStatusIs(user, Task.NOT_DONE_STATUS);
    }

    public List<Task> getTasksOnDeadline(User user, LocalDate date) {
        System.out.println(date.minusDays(user.getSettings().getDaysToDeadlineSoon()));
        return repository.findTaskByUserIdAndStartDateIsLessThanAndStopDateIsGreaterThanAndStatus(user, date, Task.NOT_DONE_STATUS, user.getSettings().getDaysToDeadlineSoon());
    }

    public void addTaskToPlan(Task task, Plan plan, long step) {
        TaskPlan taskPlan = new TaskPlan(new TaskPlanId(task.getId(), plan.getId()), task, plan, (int) step);
        taskPlanRepository.save(taskPlan);
    }

    public void updateTaskPlan(Task task, Plan plan, long step) {
        TaskPlan taskPlan = new TaskPlan(new TaskPlanId(task.getId(), plan.getId()), task, plan, (int) step);
        taskPlanRepository.save(taskPlan);
    }

    //@Transactional
    public void addTaskToPlan(Task task, Plan plan) {
        long step = taskPlanRepository.countByPlan(plan) + 1;
        TaskPlan taskPlan = new TaskPlan(new TaskPlanId(task.getId(), plan.getId()), task, plan, (int) step);
        taskPlanRepository.save(taskPlan);
    }

    public List<Task> getTasksInCategoryBetweenDates(Category category, LocalDate startDate, LocalDate endDate) {
        return repository.findTaskByCategoryIdAndDoneByTmzBetweenAndStatus(category, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), Task.DONE_STATUS);
    }

    public List<Task> getTasksBetweenDates(User user, LocalDate startDate, LocalDate endDate) {
        return repository.findTaskByUserIdAndDoneByTmzBetweenAndStatus(user, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), Task.DONE_STATUS, Sort.by(Sort.Direction.ASC, "doneBy"));
    }

    public Long getNumberOfTasksInRepeat(Task task) {
        return repository.getNumberOfTasksFromRepeat(task.getRepeatId());
    }

    public Long getNumberOfDoneTasksInRepeat(Task task) {
        return repository.getNumberOfTasksFromRepeatWithStatus(task.getRepeatId(), Task.DONE_STATUS);
    }

    public Map<Category, List<Task>> getTasksBetweenDatesInActive(User user, LocalDate startDate, LocalDate endDate) {
        List<Category> categories = categoryRepository.findCategoriesByUserIdAndOnWatchIsTrue(user);
        Map<Category, List<Task>> categoryListMap = new HashMap<>();
        for (Category cat: categories) {
            categoryListMap.put(cat, new ArrayList<Task>());
        }
        List<Task> tasks = repository.findTaskByUserIdAndDoneByTmzBetweenAndInCategories(user, startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), categories);
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

    public List<Task> filterForStorage(User user, StorageSearchDto searchDto) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<Task> cq = session.getCriteriaBuilder().createQuery(Task.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Task> task = cq.from(Task.class);
        predicates.add(criteriaBuilder.equal(task.get("userId"), user));

        if (searchDto.getText() != null) {
            //predicates.add(criteriaBuilder.like(task.get("name"), "%" + text + "%"));
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(task.get("name"), "%" + searchDto.getText() + "%"), criteriaBuilder.like(task.get("details"), "%" + searchDto.getText() + "%")));
        }

        if (searchDto.getStartDate() != null) {
            predicates.add(criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(task.get("startDate"), searchDto.getStartDate()), criteriaBuilder.greaterThanOrEqualTo(task.get("stopDate"), searchDto.getStartDate())));
        }

        if (searchDto.getStopDate() != null) {
            predicates.add(criteriaBuilder.or(criteriaBuilder.lessThanOrEqualTo(task.get("startDate"), searchDto.getStopDate()), criteriaBuilder.lessThanOrEqualTo(task.get("stopDate"), searchDto.getStopDate())));
        }

        if (searchDto.getDoneStartDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(task.get("doneByTmz"), searchDto.getDoneStartDate().atStartOfDay()));
            predicates.add(criteriaBuilder.equal(task.get("status"), Task.DONE_STATUS));
        }

        if (searchDto.getDoneStopDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(task.get("doneByTmz"), LocalDateTime.of(searchDto.getDoneStopDate(), LocalTime.MAX)));
            predicates.add(criteriaBuilder.equal(task.get("status"), Task.DONE_STATUS));
        }

        if (searchDto.getMinPoints() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(task.get("estimate"), searchDto.getMinPoints()));
        }

        if (searchDto.getMaxPoints() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(task.get("estimate"), searchDto.getMaxPoints()));
        }

        if (searchDto.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(task.get("status"), searchDto.getStatus()));
        }

        if (searchDto.getIsRepeated() != null) {
            if (searchDto.getIsRepeated()) predicates.add(criteriaBuilder.isNotNull(task.get("repeatId")));
            else predicates.add(criteriaBuilder.isNull(task.get("repeatId")));
        }

        if (searchDto.getBelongsToPlan() != null) {
            if (searchDto.getBelongsToPlan()) predicates.add(criteriaBuilder.isNotNull(task.get("plan")));
            else predicates.add(criteriaBuilder.isNull(task.get("plan")));
        }

        if (searchDto.getCategories() != null) {
            List<Category> categories = categoryRepository.findCategoriesByUserIdAndListOfIds(user, searchDto.getCategories());
            CriteriaBuilder.In<Category> inClause = criteriaBuilder.in(task.get("categoryId"));
            for (Category category : categories) {
                inClause.value(category);
            }
            predicates.add(inClause);
        }

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }

    public List<Task> createWeeklyRepeatTasks(Repeat repeat) {
        LocalDate localStartDate;
        LocalDate localEndDate;
        List<Task> tasks = new ArrayList<>();
        LocalDate counterDate = repeat.getRepeatStart();
        LocalDate stopDate = repeat.getRepeatEnd();
        counterDate = counterDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        if (repeat.getNumberOfRepeats() > 0) {
            Integer currentRepeatsNum = 0;
            while (!currentRepeatsNum.equals(repeat.getNumberOfRepeats())) {
                for (int i: repeat.getRepeatDays()) {
                    if (currentRepeatsNum.equals(repeat.getNumberOfRepeats())) {
                        break;
                    }
                    localStartDate = counterDate.plusDays(i);
                    localEndDate = null;
                    if (repeat.getStopDate() != null) {
                        localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                    }
                    Task newTask = new Task(repeat, localStartDate, localEndDate);
                    tasks.add(newTask);
                    currentRepeatsNum++;
                }
                counterDate = counterDate.plusDays(7L * repeat.getRepeatInterval());
            }
            return tasks;
        }
        if (stopDate == null) {
            stopDate = LocalDate.of(counterDate.getYear(), 12, 31);
        }
        while (!stopDate.isBefore(counterDate)) {
            for (int i: repeat.getRepeatDays()) {
                localStartDate = counterDate.plusDays(i);
                localEndDate = null;
                if (repeat.getStopDate() != null) {
                    localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                }
                Task newTask = new Task(repeat, localStartDate, localEndDate);
                tasks.add(newTask);
            }
            counterDate = counterDate.plusDays(7L * repeat.getRepeatInterval());
        }
        return tasks;
    }

    public List<Task> createDailyRepeatTasks(Repeat repeat) {
        LocalDate localStartDate;
        LocalDate localEndDate;
        List<Task> tasks = new ArrayList<>();
        LocalDate counterDate = repeat.getRepeatStart();
        LocalDate stopDate = repeat.getRepeatEnd();
        if (repeat.getNumberOfRepeats() > 0) {
            System.out.println(repeat.getNumberOfRepeats());
            for (int i = 0; i < repeat.getNumberOfRepeats(); i++) {
                localStartDate = counterDate.plusDays(1);
                localEndDate = null;
                if (repeat.getStopDate() != null) {
                    localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                }
                Task newTask = new Task(repeat, localStartDate, localEndDate);
                tasks.add(newTask);
            }
            return tasks;
        }
        if (stopDate == null) {
            stopDate = LocalDate.of(counterDate.getYear(), 12, 31);
        }
        while (stopDate.isAfter(counterDate)) {
            localStartDate = counterDate.plusDays(1);
            localEndDate = null;
            if (repeat.getStopDate() != null) {
                localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
            }
            Task newTask = new Task(repeat, localStartDate, localEndDate);
            tasks.add(newTask);
            counterDate = counterDate.plusDays(1);
        }
        return tasks;
    }

    public List<Task> createMonthlyRepeatTasks(Repeat repeat) {
        LocalDate localStartDate;
        LocalDate localEndDate;
        List<Task> tasks = new ArrayList<>();
        LocalDate counterDate = repeat.getRepeatStart();
        LocalDate stopDate = repeat.getRepeatEnd();
        if (repeat.getNumberOfRepeats() != 0) {
            Integer currentRepeatsNum = 0;
            while (!currentRepeatsNum.equals(repeat.getNumberOfRepeats())) {
                for (int i: repeat.getRepeatDays()) {
                    if (currentRepeatsNum.equals(repeat.getNumberOfRepeats())) {
                        break;
                    }
                    localStartDate = DatetimeStringFormatter.getClosestExistingMonthlyDate(counterDate, i);
                    localEndDate = null;
                    if (repeat.getStopDate() != null) {
                        localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                    }
                    Task newTask = new Task(repeat, localStartDate, localEndDate);
                    tasks.add(newTask);
                    currentRepeatsNum++;
                }
                counterDate = counterDate.plusMonths(repeat.getRepeatInterval());
            }
            return tasks;
        }
        if (stopDate == null) {
            stopDate = LocalDate.of(counterDate.getYear(), 12, 31);
        }
        while (stopDate.isAfter(counterDate)) {
            for (int i: repeat.getRepeatDays()) {
                localStartDate = DatetimeStringFormatter.getClosestExistingMonthlyDate(counterDate, i);
                localEndDate = null;
                if (repeat.getStopDate() != null) {
                    localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                }
                Task newTask = new Task(repeat, localStartDate, localEndDate);
                tasks.add(newTask);
            }
            counterDate = counterDate.plusMonths(repeat.getRepeatInterval());
        }
        return tasks;
    }

    public List<Task> createYearlyRepeatTasks(Repeat repeat) {
        LocalDate localStartDate;
        LocalDate localEndDate;
        List<Task> tasks = new ArrayList<>();
        LocalDate counterDate = repeat.getRepeatStart();
        LocalDate stopDate = repeat.getRepeatEnd();
        if (repeat.getNumberOfRepeats() != 0) {
            for (int i = 0; i < repeat.getNumberOfRepeats(); i++) {
                localStartDate = counterDate.plusYears(repeat.getRepeatInterval());
                localEndDate = null;
                if (repeat.getStopDate() != null) {
                    localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                }
                Task newTask = new Task(repeat, localStartDate, localEndDate);
                tasks.add(newTask);
            }
            return tasks;
        }
        if (stopDate == null) {
            stopDate = LocalDate.of(counterDate.plusYears(1).getYear(), 12, 31);
        }
        while (stopDate.isAfter(counterDate)) {
            localStartDate = counterDate.plusYears(1);
            localEndDate = null;
            if (repeat.getStopDate() != null) {
                localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
            }
            Task newTask = new Task(repeat, localStartDate, localEndDate);
            tasks.add(newTask);
            counterDate = counterDate.plusYears(repeat.getRepeatInterval());
        }
        return tasks;
    }

    public void createTasksForRepeat(Repeat repeat) {
        List<Task> tasks = new ArrayList<>();
        switch (repeat.getRepeatTerm()) {
            case Repeat.DAILY -> tasks = createDailyRepeatTasks(repeat);
            case Repeat.WEEKLY -> tasks = createWeeklyRepeatTasks(repeat);
            case Repeat.MONTHLY -> tasks = createMonthlyRepeatTasks(repeat);
            case Repeat.YEARLY -> tasks = createYearlyRepeatTasks(repeat);
        }
        Iterable<Task> savedTasks = repository.saveAll(tasks);

        if (repeat.getPlanId() != null) {
            for (Task task: savedTasks) {
                Plan plan = repeat.getPlanId();
                long step = taskPlanRepository.countByPlan(plan);;
                addTaskToPlan(task, plan, step);
            }
        }
    }

    @Transactional
    public void deleteInRepeatAfter(Long taskId) {
        Task task = repository.findById(taskId).orElseThrow();
        repository.deleteByRepeatIdAndStartDateIsGreaterThanEqual(task.getRepeatId(), task.getStartDate());
    }

    @Transactional
    public void deleteAllInRepeat(Long taskId) {
        Task task = repository.findById(taskId).orElseThrow();
        repository.deleteByRepeatId(task.getRepeatId());
    }

    public void deleteRepeatedTasks(Long taskId, String regime) throws NoSuchTaskException {
        switch (regime) {
            case DeleteRegimes.DELETE_ONE -> delete(taskId);
            case DeleteRegimes.DELETE_AFTER -> deleteInRepeatAfter(taskId);
            case DeleteRegimes.DELETE_ALL -> deleteAllInRepeat(taskId);
        }
    }
}
