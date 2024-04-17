package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @ManyToOne
    @MapsId("taskId")
    private Task task;
    @ManyToOne
    @MapsId("planId")
    private Plan plan;
    @Column(name = "step_number")
    private int stepNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof TaskPlan))
            return false;

        return
                id != null &&
                        id.equals(((TaskPlan) o).getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
