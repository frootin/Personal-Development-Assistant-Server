package ru.sfu.controllers;


import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.db.repositories.UserRepository;
import ru.sfu.db.services.UserService;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.db.repositories.TaskRepository;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.HomeDto;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final TaskRepository taskRepository;
    private final UserService userRepository;


    public ApiController(TaskRepository taskRepository, UserService userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value="/{year}/{month}/{day}")
    @ResponseBody
    public HomeDto getMainPageForThreeDays(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        LocalDate centeredDate = DatetimeStringFormatter.getDateFromYearMonthDay(year, month, day);
        User curUser = userRepository.findById(1L);
        return HomeJsonFormatter.getHomeDtoFromRepositories(taskRepository, curUser, centeredDate);
    }

}
