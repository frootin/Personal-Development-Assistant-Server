package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeDto {
    private DayDto yesterday;
    private DayDto today;
    private DayDto tomorrow;
    @JsonProperty("free_tasks")
    private List<TaskDto> freeTasks;
    @JsonProperty("late_tasks")
    private List<TaskDto> lateTasks;
    @JsonProperty("soon_tasks")
    private List<TaskDto> soonTasks;
}
