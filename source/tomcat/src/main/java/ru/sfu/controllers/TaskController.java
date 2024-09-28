package ru.sfu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.*;
import ru.sfu.db.services.PlanService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.CatService;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.PlanDto;
import ru.sfu.objects.RepeatDto;
import ru.sfu.objects.TaskWindowDto;
import java.util.List;
import java.util.Map;

import ru.sfu.exceptions.*;
import ru.sfu.db.services.UserService;
import ru.sfu.util.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskService;
    UserService userService;
    CatService categoryService;
    PlanService planService;

    @PostMapping
    public TaskWindowDto createTask(@RequestBody JsonNode json) {
        TaskWindowDto taskDto = JsonUtil.JsonToDto(json, TaskWindowDto.class);
        assert taskDto != null;
        Task task = JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class);
        assert task != null;
        if (taskDto.getRepeat() != null) {
            RepeatDto repeatDto = taskDto.getRepeat();
            Repeat repeat = new Repeat(task, repeatDto.getTerm(), repeatDto.getDays(), repeatDto.getStart(), repeatDto.getEnd());
        }
        return JsonUtil.ModelToDto(taskService.save(task), TaskWindowDto.class);
        /**if (taskDto.getPlanDto() != null) {
            taskService.addTaskToPlan(newtask, planService.findById(taskDto.getPlanDto().getId()));
        }*/
    }

    @GetMapping("{id}")
    public TaskWindowDto getTask(@PathVariable long id) throws NoSuchTaskException {
        Task task = taskService.findById(id);
        ModelMapper modelMapper = new ModelMapper();
        TaskWindowDto taskDto = modelMapper.map(task, TaskWindowDto.class);
        TaskPlan taskPlan = task.getPlan();
        if (taskPlan != null) {
            TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
            propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
            taskDto.setPlanDto(JsonUtil.mapModelWithSkips(taskPlan.getPlan(), PlanDto.class, modelMapper));
        }
        /**if (task == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundStudent);
        }*/
        return taskDto;
    }

    @GetMapping
    public ResponseEntity<List<TaskWindowDto>> getTasks() {
        List<Task> tasks = taskService.getTasksForUser(userService.findById(1L));
        List<TaskWindowDto> dtoTasks = JsonUtil.mapList(tasks,TaskWindowDto.class);
        //ResponseEntity.status()
        return ResponseEntity.ok(dtoTasks);
    }

    @PutMapping
    public void updateTask(@RequestBody JsonNode json) {
        taskService.save(JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) throws NoSuchTaskException {
        taskService.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable long id, @RequestBody JsonPatch patch) {
        try {
            Task task = taskService.findById(id);
            Task taskPatched = JsonUtil.applyPatch(patch, task, Task.class);
            taskService.save(taskPatched);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NoSuchTaskException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
