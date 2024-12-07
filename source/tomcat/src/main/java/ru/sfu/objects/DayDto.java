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
public class DayDto {
    @JsonProperty("fixed_tasks")
    private List<TaskDto> fixedTasks;
    @JsonProperty("done_tasks")
    private List<TaskDto> doneTasks;
    private String date;
    @JsonProperty("text_note")
    private DayNoteDto textNote;
    @JsonProperty("day_classes")
    private List<EventDetailsDto> eventDtos;
}
