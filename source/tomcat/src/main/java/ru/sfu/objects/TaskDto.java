package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private long id;
    private String name;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("stop_time")
    private String stopTime;
    private int status;
}
