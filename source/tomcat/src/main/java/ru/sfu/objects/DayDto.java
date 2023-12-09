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
    //@SerializedName("fixed_tasks")
    @JsonProperty("fixed_tasks")
    private List<TaskDto> fixedTasks;
    //@SerializedName("done_tasks")
    @JsonProperty("done_tasks")
    private List<TaskDto> doneTasks;
    //@SerializedName("date")
    private String date;
    //@SerializedName("text_note")
    @JsonProperty("text_note")
    private String textNote;
    //@SerializedName("day_classes")
    @JsonProperty("day_classes")
    private List<StudyClass> studyClasses;
}
