package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Plan;
import ru.sfu.db.services.CatService;
import ru.sfu.db.services.PlanService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.PlanDto;
import java.util.List;
import ru.sfu.db.services.UserService;
import ru.sfu.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/plans")
public class PlanController {
    PlanService planRepository;
    UserService userService;

    public PlanController(PlanService planRepository, UserService userService) {
        this.planRepository = planRepository;
        this.userService = userService;
    }
    
    @PostMapping
    public void createPLan(@RequestBody JsonNode json) {
        planRepository.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
    }

    @GetMapping("{id}")
    public PlanDto readPlan(@PathVariable long id) {
        Plan plan = planRepository.findById(id);
        return JsonUtil.ModelToDto(plan, PlanDto.class);
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

    @PutMapping
    public void updatePLan(@RequestBody JsonNode json) {
        planRepository.save(JsonUtil.JsonToSingleModel(json, PlanDto.class, Plan.class));
    }

    @DeleteMapping("{id}")
    public void deletePlan(@PathVariable long id) {
        planRepository.delete(id);
    }
}
