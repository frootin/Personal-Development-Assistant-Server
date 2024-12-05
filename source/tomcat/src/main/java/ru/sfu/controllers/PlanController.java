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
import ru.sfu.db.services.PlanService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.exceptions.NoSuchTaskException;
import ru.sfu.objects.CategoryDto;
import ru.sfu.objects.PlanDto;
import ru.sfu.objects.PlansPageDto;
import ru.sfu.objects.TaskDto;
import ru.sfu.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {
    PlanService planService;
    UserService userService;
    TaskService taskService;
    
    @PostMapping
    public PlanDto createPLan(@RequestBody JsonNode json) {
        Plan plan = planService.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
        return JsonUtil.ModelToDto(plan, PlanDto.class);
    }

    @GetMapping("{id}")
    public PlanDto readPlan(@PathVariable long id) {
        Plan plan = planService.findById(id);
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
        List<Plan> plans = planService.getPlansForUser(userService.findById(1L));
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
        propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
        List<PlanDto> dtoPlans = JsonUtil.mapListWithSkips(plans, PlanDto.class, modelMapper);
        return dtoPlans;
    }

    @GetMapping("full")
    public List<PlanDto> getFull(@RequestParam(required = false) Integer status) {
        List<PlanDto> activeDtos = new ArrayList<>();
        List<Plan> plans;
        if (status != null) {
            plans = planService.getFullPlansForUserByStatus(userService.findById(1L), status.intValue());
        } else {
            plans = planService.getFullPlansForUser(userService.findById(1L));
        }
        for (Plan plan: plans) {
            activeDtos.add(planService.getFullDtoFromPlan(plan, taskService));
        }
        return activeDtos;
    }

    @GetMapping("full/active")
    public List<PlanDto> getFullActive() {
        List<PlanDto> activeDtos = new ArrayList<>();
        for (Plan plan: planService.getFullPlansForUserByStatus(userService.findById(1L), Plan.NOT_DONE_STATUS)) {
            activeDtos.add(planService.getFullDtoFromPlan(plan, taskService));
        }
        return activeDtos;
    }

    @GetMapping("full/archive")
    public List<PlanDto> getFullArchive() {
        List<PlanDto> archiveDtos = new ArrayList<>();
        for (Plan plan: planService.getFullPlansForUserByStatus(userService.findById(1L), Plan.DONE_STATUS)) {
            archiveDtos.add(planService.getFullDtoFromPlan(plan, taskService));
        }
        return archiveDtos;
    }

    @PutMapping
    public PlanDto updatePLan(@RequestBody JsonNode json) {
        Plan plan = planService.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
        return JsonUtil.ModelToDto(plan, PlanDto.class);
    }

    @DeleteMapping("{id}")
    public void deletePlan(@PathVariable long id) {
        planService.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Plan> patchPlan(@PathVariable long id, @RequestBody JsonPatch patch) {
        try {
            Plan plan = planService.findById(id);
            Plan planPatched = JsonUtil.applyPatch(patch, plan, Plan.class);
            planService.save(planPatched);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
