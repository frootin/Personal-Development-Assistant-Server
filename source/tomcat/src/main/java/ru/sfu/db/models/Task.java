package ru.sfu.db.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
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
}
