package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.TaskPlan;
import ru.sfu.util.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWindowDto {
    private long id;
    private String name;

    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("description")
    private String details;

    @JsonProperty("start_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonProperty("stop_date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate stopDate;

    @JsonProperty("start_time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime startTime;

    @JsonProperty("stop_time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime stopTime;

    @JsonProperty("task_category")
    private CategoryDto category;

    private int estimate;


    private RepeatDto repeat;
    private int status;
    private String timezone;

    @JsonProperty("done_by")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime doneBy;

    @JsonProperty("plan")
    private PlanDto planDto;

    @JsonProperty("plan_id")
    private Long referId;

    public void setPlan(Task task) {
        ModelMapper modelMapper = new ModelMapper();
        TaskPlan taskPlan = task.getPlan();
        if (taskPlan != null) {
            TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
            propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
            this.setPlanDto(JsonUtil.mapModelWithSkips(taskPlan.getPlan(), PlanDto.class, modelMapper));
        }
    }
}
