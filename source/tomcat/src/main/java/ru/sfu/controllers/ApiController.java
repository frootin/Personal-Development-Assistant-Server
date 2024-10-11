package ru.sfu.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.User;
import ru.sfu.db.services.DayNoteService;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.db.services.EventService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.HomeDto;
import ru.sfu.objects.TaskWindowDto;
import ru.sfu.util.JsonUtil;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final EventService eventService;
    private final UserService userRepository;
    private final TaskService taskService;
    private final DayNoteService dayNoteService;

    @GetMapping(value="/{year}/{month}/{day}")
    @ResponseBody
    public HomeDto getMainPageForThreeDays(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        LocalDate centeredDate = DatetimeStringFormatter.getDateFromYearMonthDay(year, month, day);
        User curUser = userRepository.findById(1L);
        return HomeJsonFormatter.getHomeDtoFromRepositories(taskService, eventService, dayNoteService, curUser, centeredDate);
    }

    @GetMapping("/filtered")
    @ResponseBody
    public List<TaskWindowDto> getFiltered(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String details,
                                           @RequestParam(required = false) Integer status) {
        User curUser = userRepository.findById(1L);
        return JsonUtil.mapList(taskService.filterByFields(curUser, name, details, status), TaskWindowDto.class);
    }

}
