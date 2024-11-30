package ru.sfu.db.models;

import javax.persistence.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    public static final int DONE_STATUS = 1;
    public static final int NOT_DONE_STATUS = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @Column(name = "task_name")
    private String name;
    @Column(name="details")
    private String details;
    @Column(name="estimate")
    private int estimate;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category categoryId;
    @Column(name = "date_start")
    private LocalDate startDate;
    @Column(name = "date_end")
    private LocalDate stopDate;
    @Column(name = "time_start")
    private LocalTime startTime;
    @Column(name = "time_end")
    private LocalTime stopTime;
    @Column(name = "task_timezone")
    private String timezone;
    @Column(name = "status")
    private int status;
    @Column(name = "done_by")
    private LocalDateTime doneBy;
    @ManyToOne
    @JoinColumn(name = "repeat_id", referencedColumnName = "id", nullable = true)
    private Repeat repeatId;
    @JsonBackReference
    @OneToOne(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private TaskPlan plan;
    /**
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "tasks_in_plans",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "plan_id", referencedColumnName = "id"))
    private Plan plan;*/

    public Task(User userId, String name, String details, int estimate, Category categoryId, LocalDate startDate,
                LocalDate stopDate, LocalTime startTime, LocalTime stopTime, String timezone, int status, LocalDateTime doneBy,
                Repeat repeatId) {
        this.userId = userId;
        this.name = name;
        this.details = details;
        this.estimate = estimate;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.timezone = timezone;
        this.status = status;
        this.doneBy = doneBy;
        this.repeatId = repeatId;
    }

    public Task(User userId, String name, String details, int estimate, Category categoryId, LocalDate startDate,
                LocalDate stopDate, LocalTime startTime, LocalTime stopTime, String timezone, int status, LocalDateTime doneBy) {
        this.userId = userId;
        this.name = name;
        this.details = details;
        this.estimate = estimate;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.timezone = timezone;
        this.status = status;
        this.doneBy = doneBy;
        //this.repeatId = repeatId;
    }

    public Task(long id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Task(String name, String details, LocalDate startDate, LocalDate stopDate, Category categoryId, String timezone, int estimate, LocalDateTime doneBy, int status) {
        this.name = name;
        this.details = details;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.categoryId = categoryId;
        this.timezone = timezone;
        this.estimate = estimate;
        this.doneBy = doneBy;
        this.status = status;
    }

    public Task(Repeat repeat, LocalDate startDate, LocalDate stopDate) {
        this.name = repeat.getName();
        this.details = repeat.getDetails();
        this.categoryId = repeat.getCategoryId();
        this.estimate = repeat.getEstimate();
        this.userId = repeat.getUserId();
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.startTime = repeat.getStartTime();
        this.stopTime = repeat.getStopTime();
        this.timezone = repeat.getTimezone();
        this.status = NOT_DONE_STATUS;
        this.repeatId = repeat;
    }

    public List<Task> getTasksForWeekFromRepeat(Repeat repeat) {
        LocalDate localStartDate;
        LocalDate localEndDate;
        List<Task> tasks = new ArrayList<>();
        LocalDate counterDate = repeat.getStartDate();
        LocalDate stopDate = repeat.getStopDate();
        if (stopDate == null) {
            stopDate = LocalDate.of(counterDate.getYear(), 12, 31);
        }
        counterDate = counterDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        while (stopDate.isAfter(counterDate)) {
            for (int i: repeat.getRepeatDays()) {
                localStartDate = counterDate.plusDays(i - 1);
                localEndDate = localStartDate.plusDays(ChronoUnit.DAYS.between(repeat.getStartDate(), repeat.getStopDate()));
                tasks.add(new Task(repeat, localStartDate, localEndDate));
            }
            counterDate = counterDate.plusDays(7);
        }
        return tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDate stopDate) {
        this.stopDate = stopDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalTime stopTime) {
        this.stopTime = stopTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(LocalDateTime doneBy) {
        this.doneBy = doneBy;
    }

    public TaskPlan getPlan() {
        return plan;
    }

    public void setPlan(TaskPlan plan) {
        this.plan = plan;
    }

    public void setStatus(int status) {
        this.status = status;
        if (status == 1) {
            this.doneBy = LocalDateTime.now(ZoneOffset.UTC);
        }
    }

    @PreUpdate
    public void updateDoneBy() {
        if (this.status == 1) {
            this.doneBy = LocalDateTime.now(ZoneOffset.UTC);
        }
    }

    public Repeat getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(Repeat repeatId) {
        this.repeatId = repeatId;
    }
}
