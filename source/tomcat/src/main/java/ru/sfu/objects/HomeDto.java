package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.google.gson.annotations.SerializedName;
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
    //@SerializedName("free_tasks")
    @JsonProperty("free_tasks")
    private List<TaskDto> freeTasks;
    //@SerializedName("late_tasks")
    @JsonProperty("late_tasks")
    private List<TaskDto> lateTasks;
}
