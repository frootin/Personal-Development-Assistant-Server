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
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.TaskPlan;
import ru.sfu.db.services.PlanService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.CatService;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.PlanDto;
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
    public void createTask(@RequestBody JsonNode json) {
        Task task = JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class);
        taskService.save(task);
    }

    @GetMapping("{id}")
    public TaskWindowDto getTask(@PathVariable long id) throws NoSuchTaskException {
        Task task = taskService.findById(id);
        List<Category> categories = categoryService.getCategoriesForUser(task.getUserId());
        ModelMapper modelMapper = new ModelMapper();
        TaskWindowDto taskDto = modelMapper.map(task, TaskWindowDto.class);
        taskDto.setAllUserCategories(JsonUtil.mapList(categories, CategoryDto.class));
        TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
        propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
        TaskPlan taskPlan = task.getPlan();
        if (taskPlan != null) {
            taskDto.setPlanDto(JsonUtil.mapModelWithSkips(taskPlan.getPlan(), PlanDto.class, modelMapper));
        }
        return taskDto;
    }

    @GetMapping
    public List<TaskWindowDto> getTasks() {
        List<Task> tasks = taskService.getTasksForUser(userService.findById(1L));
        List<TaskWindowDto> dtoTasks = JsonUtil.mapList(tasks,TaskWindowDto.class);
        return dtoTasks;
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
