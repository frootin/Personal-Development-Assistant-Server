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
    @JsonProperty("day_of_week")
    private String dayOfWeek;
    @JsonProperty("day_classes")
    private List<EventDto> dayClasses;
}
