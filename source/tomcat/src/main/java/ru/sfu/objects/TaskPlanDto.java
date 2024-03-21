package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPlanDto {
    private TaskDto task;
    @JsonProperty("step_number")
    private int stepNumber;
}
