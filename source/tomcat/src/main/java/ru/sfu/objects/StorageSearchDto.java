package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageSearchDto {
    @JsonProperty("entity_types")
    private List<String> entityTypes;
    private Integer status;
    private String text;
    @JsonProperty("start_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;
    @JsonProperty("stop_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate stopDate;
    @JsonProperty("done_start_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate doneStartDate;
    @JsonProperty("done_stop_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate doneStopDate;

    private List<Long> categories;
    @JsonProperty("is_repeated")
    private Boolean isRepeated;
    @JsonProperty("belongs_to_plan")
    private Boolean belongsToPlan;
    @JsonProperty("min_points")
    private Integer minPoints;
    @JsonProperty("max_points")
    private Integer maxPoints;
}
