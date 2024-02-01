package ru.sfu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Task;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.db.services.TaskService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.TaskWindowDto;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskRepository;
    CategoryRepository categoryRepository;

    public TaskController(TaskService taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public void createTask(@RequestBody JsonNode json) {
        ObjectMapper jsonMapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();
        try {
            TaskWindowDto taskDto = jsonMapper.treeToValue(json, TaskWindowDto.class);
            Task task = modelMapper.map(taskDto, Task.class);
            taskRepository.save(task);
        } catch (JsonProcessingException e) {System.out.println(e.getMessage());}
    }

    @GetMapping("{id}")
    public TaskWindowDto getTask(@PathVariable long id) {
        Task task = taskRepository.findById(id);
        List<Category> categories = categoryRepository.findCategoriesByUserId(task.getUserId());
        ModelMapper modelMapper = new ModelMapper();
        TaskWindowDto taskDto = modelMapper.map(task, TaskWindowDto.class);
        taskDto.setAllUserCategories(HomeJsonFormatter.mapList(categories, CategoryDto.class));
        return taskDto;
    }

    @PutMapping
    public void updateTask(@RequestBody JsonNode json) {
        ObjectMapper jsonMapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();
        try {
            TaskWindowDto taskDto = jsonMapper.treeToValue(json, TaskWindowDto.class);
            Task task = modelMapper.map(taskDto, Task.class);
            taskRepository.save(task);
        } catch (JsonProcessingException e) {System.out.println(e.getMessage());}
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskRepository.delete(id);
    }
}
