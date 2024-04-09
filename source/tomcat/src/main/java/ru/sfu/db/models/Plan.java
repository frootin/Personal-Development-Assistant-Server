package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "Plan.tasks",
        includeAllAttributes = true
)
@NamedEntityGraph(name = "notasks",
       attributeNodes = {@NamedAttributeNode("status"),
                         @NamedAttributeNode("name"),
                         @NamedAttributeNode("details")
                         }
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
    @Column(name = "status")
    private int status;
    @OneToMany(
            mappedBy = "plan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TaskPlan> tasks;
    /**@OneToMany(cascade = CascadeType.ALL, mappedBy = "plan", fetch = FetchType.EAGER)
    private List<Task> tasks;*/

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
        if (this.tasks != null) {
            this.tasks.clear();
            this.tasks.addAll(tasks);
        }
        this.tasks = tasks;
    }
}