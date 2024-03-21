package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sfu.db.models.Task;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {
    private long id;
    //@JsonProperty("user_id")
    //private long userId;
    private String name;
    private String details;
    private int status;
    private List<TaskPlanDto> tasks;
}
