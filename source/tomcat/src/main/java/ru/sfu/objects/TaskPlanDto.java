package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPlanDto {
    @JsonProperty("task_id")
    private long taskId;
    @JsonProperty("plan_id")
    private long planId;
    @JsonProperty("step_number")
    private int stepNumber;
}
