package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.services.UserService;
import ru.sfu.db.services.WheelService;
import ru.sfu.objects.WheelCategoryDto;
import ru.sfu.util.JsonUtil;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/wheel")
public class WheelController {
    public WheelService wheelService;
    public UserService userService;

    @GetMapping
    public List<WheelCategoryDto> getWheelForDates(@RequestBody JsonNode json) {
        LocalDate startDate = JsonUtil.getDateFromJson(json, "start_date");
        LocalDate endDate = JsonUtil.getDateFromJson(json, "end_date");
        return wheelService.getPointsForCategories(userService.findById(1L), startDate, endDate);
    }

    @PostMapping
    public List<WheelCategoryDto> getWheelForDatesPost(@RequestBody JsonNode json) {
        LocalDate startDate = JsonUtil.getDateFromJson(json, "start_date");
        LocalDate endDate = JsonUtil.getDateFromJson(json, "end_date");
        return wheelService.getPointsForCategories(userService.findById(1L), startDate, endDate);
    }
}
