package ru.sfu.controllers;


import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.User;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.db.services.EventService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.HomeDto;
import ru.sfu.objects.TaskWindowDto;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiController {
    private final EventService eventService;
    private final UserService userRepository;
    private final TaskService taskService;
    


    public ApiController(UserService userRepository, TaskService taskService, EventService eventService) {
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    @GetMapping(value="/{year}/{month}/{day}")
    @ResponseBody
    public HomeDto getMainPageForThreeDays(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        LocalDate centeredDate = DatetimeStringFormatter.getDateFromYearMonthDay(year, month, day);
        User curUser = userRepository.findById(1L);
        return HomeJsonFormatter.getHomeDtoFromRepositories(taskService, eventService, curUser, centeredDate);
    }

    @GetMapping("/filtered")
    @ResponseBody
    public List<TaskWindowDto> getFiltered(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String details,
                                           @RequestParam(required = false) Integer status) {
        User curUser = userRepository.findById(1L);
        return HomeJsonFormatter.mapList(taskService.filterByFields(curUser, name, details, status), TaskWindowDto.class);
    }

}
