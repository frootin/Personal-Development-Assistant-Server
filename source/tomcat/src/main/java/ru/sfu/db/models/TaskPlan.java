package ru.sfu.db.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks_in_plans")
public class TaskPlan {
    @EmbeddedId
    private TaskPlanId id;
    @ManyToOne
    @MapsId("taskId")
    private Task task;
    @ManyToOne
    @MapsId("planId")
    private Plan plan;
    @Column(name = "step_number")
    private int stepNumber;
}
