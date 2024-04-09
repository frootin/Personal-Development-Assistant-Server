package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.services.PlanService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.exceptions.NoSuchTaskException;
import ru.sfu.objects.TaskWindowDto;
import ru.sfu.util.JsonUtil;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/taskplans")
public class TaskPlanController {
    TaskService taskService;
    UserService userService;
    PlanService planService;

    @PostMapping
    public void createTaskInPlan(@RequestBody JsonNode json) throws NoSuchTaskException {
        Task task = taskService.findById(json.get("task_id").asLong());
        Plan plan = planService.findById(json.get("plan_id").asLong());
        long step = json.get("step_number").asLong(planService.getNumberOfTasksInPLan(plan) + 1);
        taskService.addTaskToPlan(task, plan, step);
    }
}
