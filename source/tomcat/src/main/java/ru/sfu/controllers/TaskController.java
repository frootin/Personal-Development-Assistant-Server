package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Task;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.CatService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.TaskWindowDto;
import java.util.List;
import ru.sfu.exceptions.*;
import ru.sfu.db.services.UserService;
import ru.sfu.util.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskRepository;
    UserService userService;
    CatService categoryRepository;

    @PostMapping
    public void createTask(@RequestBody JsonNode json) {
        taskRepository.save(JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class));
    }

    @GetMapping("{id}")
    public TaskWindowDto getTask(@PathVariable long id) throws NoSuchTaskException {
        Task task = taskRepository.findById(id);
        List<Category> categories = categoryRepository.getCategoriesForUser(task.getUserId());
        ModelMapper modelMapper = new ModelMapper();
        TaskWindowDto taskDto = modelMapper.map(task, TaskWindowDto.class);
        taskDto.setAllUserCategories(JsonUtil.mapList(categories, CategoryDto.class));
        return taskDto;
    }

    @GetMapping
    public List<TaskWindowDto> getTasks() {
        List<Task> tasks = taskRepository.getTasksForUser(userService.findById(1L));
        List<TaskWindowDto> dtoTasks = JsonUtil.mapList(tasks,TaskWindowDto.class);
        return dtoTasks;
    }

    @PutMapping
    public void updateTask(@RequestBody JsonNode json) {
        taskRepository.save(JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) throws NoSuchTaskException {
        taskRepository.delete(id);
    }
}
