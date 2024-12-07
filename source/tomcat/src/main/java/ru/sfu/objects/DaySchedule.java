package ru.sfu.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedule {
    @JsonProperty("day_by_num_order")
    private int dayByOrder;
    @JsonProperty("odd_week")
    private List<EventDetailsDto> oddWeek;
    @JsonProperty("even_week")
    private List<EventDetailsDto> evenWeek;
}
