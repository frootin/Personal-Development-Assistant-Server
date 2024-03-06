package ru.sfu.objects;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekSchedule {
    @JsonProperty("odd_week")
    private List<DaySchedule> oddWeek;
    @JsonProperty("even_week")
    private List<DaySchedule> evenWeek;
}
