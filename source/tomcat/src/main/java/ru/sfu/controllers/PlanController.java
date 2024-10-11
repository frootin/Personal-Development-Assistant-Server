package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.TaskPlan;
import ru.sfu.db.services.PlanService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.PlanDto;
import ru.sfu.objects.TaskDto;
import ru.sfu.objects.TaskWindowDto;
import ru.sfu.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {
    PlanService planRepository;
    UserService userService;
    TaskService taskService;
    
    @PostMapping
    public PlanDto createPLan(@RequestBody JsonNode json) {
        Plan plan = planRepository.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
        return JsonUtil.ModelToDto(plan, PlanDto.class);
    }

    @GetMapping("{id}")
    public PlanDto readPlan(@PathVariable long id) {
        Plan plan = planRepository.findById(id);
        //List<Task> tasks = plan.getTasks().stream().map(TaskPlan::getTask).toList();
        List<Task> tasks = taskService.getTasksForPlan(plan);
        List<Category> categories = tasks.stream().map(Task::getCategoryId).distinct().toList();
        int overallSum = tasks.stream().mapToInt(item -> item.getEstimate()).sum();
        int doneSum = tasks.stream().filter(c -> c.getStatus() == Task.DONE_STATUS).mapToInt(item -> item.getEstimate()).sum();
        PlanDto planDto = JsonUtil.ModelToDto(plan, PlanDto.class);
        planDto.setCategories(JsonUtil.mapList(categories, CategoryDto.class));
        planDto.setGoalPoints(overallSum);
        planDto.setDonePoints(doneSum);
        planDto.setTasks(JsonUtil.mapList(tasks, TaskDto.class));
        return planDto;
    }

    @GetMapping
    public List<PlanDto> getPlans() {
        List<Plan> plans = planRepository.getPlansForUser(userService.findById(1L));
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
        propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
        List<PlanDto> dtoPlans = JsonUtil.mapListWithSkips(plans, PlanDto.class, modelMapper);
        return dtoPlans;
    }

    @GetMapping("full")
    public List<PlanDto> getFull() {
        List<Plan> plans = planRepository.getFullPlansForUser(userService.findById(1L));
        List<PlanDto> dtos = new ArrayList<>();
        //List<PlanDto> dtoTasks = JsonUtil.mapList(tasks,TaskWindowDto.class);
        for (Plan plan: plans) {
            //List<Task> tasks = plan.getTasks().stream().map(TaskPlan::getTask).toList();
            List<Task> tasks = taskService.getTasksForPlan(plan);
            List<Category> categories = tasks.stream().map(Task::getCategoryId).distinct().toList();
            int overallSum = tasks.stream().mapToInt(item -> item.getEstimate()).sum();
            int doneSum = tasks.stream().filter(c -> c.getStatus() == Task.DONE_STATUS).mapToInt(item -> item.getEstimate()).sum();
            PlanDto planDto = JsonUtil.ModelToDto(plan, PlanDto.class);
            planDto.setCategories(JsonUtil.mapList(categories, CategoryDto.class));
            planDto.setGoalPoints(overallSum);
            planDto.setDonePoints(doneSum);
            planDto.setTasks(JsonUtil.mapList(tasks, TaskDto.class));
            dtos.add(planDto);
        }
        return dtos;
    }

    @PutMapping
    public void updatePLan(@RequestBody JsonNode json) {
        planRepository.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
    }

    @DeleteMapping("{id}")
    public void deletePlan(@PathVariable long id) {
        planRepository.delete(id);
    }
}
