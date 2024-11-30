package ru.sfu.db.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "Plan.tasks",
        includeAllAttributes = true
)
@Table(name = "user_plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @Column(name = "plan_name")
    private String name;
    @Column
    private String details;
    @Column(name = "date_start")
    private LocalDate startDate;
    @Column(name = "date_end")
    private LocalDate stopDate;
    @Column(name = "status")
    private int status;
    @OneToMany(
            mappedBy = "plan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TaskPlan> tasks = new ArrayList<>();

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TaskPlan> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskPlan> tasks) {
        if (tasks == null) return;
        if (this.tasks != null) {
            this.tasks.clear();
            this.tasks.addAll(tasks);
        } else {
            //this.tasks = new ArrayList<>();
            this.tasks.addAll(tasks);
        }
    }

    public void addTask(TaskPlan comment) {
        tasks.add(comment);
        comment.setPlan(this);
    }

    public void removeTask(TaskPlan comment) {
        tasks.remove(comment);
        comment.setPlan(null);
    }

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Plan other = (Plan) obj;
        if (id == null) {
            return false;
        } else return id.equals(other.id);
    }
}