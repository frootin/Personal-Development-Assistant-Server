package ru.sfu.db.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Repeat;
import ru.sfu.db.models.Task;
import ru.sfu.db.repositories.RepeatRepository;
import ru.sfu.objects.TaskDto;
import ru.sfu.objects.TaskWindowDto;
import ru.sfu.util.JsonUtil;

@Service
public class RepeatService {
    private RepeatRepository repository;

    @Autowired
    public RepeatService(RepeatRepository repository) {
        this.repository = repository;
    }

    public Repeat save(Repeat repeat) {
        if (repeat.getRepeatEnd() != null) {
            if (repeat.getRepeatStart().isAfter(repeat.getRepeatEnd())) {
                return null;
            }
        }
        /**if (repeat.getStartDate().equals(repeat.getStopDate()) & repeat.getStartTime().isAfter(repeat.getStopTime())) return null;
        if (repeat.getEstimate() > 100) return null;*/
        return repository.save(repeat);
    }

    /**public Repeat getRepeatFromTaskDto(JsonNode json) {
        JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class)
    }*/
}
