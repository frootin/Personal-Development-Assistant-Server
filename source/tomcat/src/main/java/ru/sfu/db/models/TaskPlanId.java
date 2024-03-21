package ru.sfu.db.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TaskPlanId implements Serializable {
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "plan_id")
    private Long planId;
}
