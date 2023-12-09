package ru.sfu.controllers;


import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.db.repositories.UserRepository;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.db.repositories.TaskRepository;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.HomeDto;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/home")
public class ApiController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    //private Gson gson = new GsonBuilder().serializeNulls().create();

    public ApiController(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(value="/{year}/{month}/{day}")
    @ResponseBody
    public HomeDto getMainPageForThreeDays(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        LocalDate centeredDate = DatetimeStringFormatter.getDateFromYearMonthDay(year, month, day);
        User curUser = userRepository.findById(1L).get();
        //Category curCat = categoryRepository.findById(3L).get();
        //taskRepository.save(new Task(curUser,"Задание", "Детали", 0, curCat, null, null, null, null, "", 0, null));
        return HomeJsonFormatter.getHomeDtoFromRepositories(taskRepository, curUser, centeredDate);
    }
}
