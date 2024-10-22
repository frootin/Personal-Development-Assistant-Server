package ru.sfu.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sfu.db.models.User;

import javax.persistence.*;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailsDto {
    private Long id;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("week_num")
    private int weekNum;
    @JsonProperty("day_of_week")
    private int dayOfWeek;
    @JsonProperty("event_name")
    private String eventName;
    private String place;
    @JsonProperty("event_format")
    private String eventFormat;

    @JsonProperty("start_time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime startTime;

    @JsonProperty("stop_time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime stopTime;
}
