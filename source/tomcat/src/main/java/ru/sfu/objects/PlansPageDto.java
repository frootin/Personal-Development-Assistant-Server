package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlansPageDto {
    @JsonProperty("active_plans")
    private List<PlanDto> freeTasks;
    @JsonProperty("archived_plans")
    private List<PlanDto> lateTasks;
}
