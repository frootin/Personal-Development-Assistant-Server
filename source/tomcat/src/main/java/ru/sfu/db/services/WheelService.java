package ru.sfu.db.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.CategoryRepository;
import ru.sfu.objects.WheelCategoryDto;


@Service
public class WheelService {
    private CategoryRepository categoryRepository;
    private TaskService taskService;

    @Autowired
    public WheelService(CategoryRepository categoryRepository, TaskService taskService) {
        this.categoryRepository = categoryRepository;
        this.taskService = taskService;
    }

    public List<WheelCategoryDto> getPointsForCategories(User user, LocalDate startDate, LocalDate endDate) {
        List<Task> tasks = taskService.getTasksBetweenDatesInActive(user, startDate, endDate);
        System.out.println(tasks.size());
        Map<Category, List<Task>> tasksByCategory = tasks.stream().collect(Collectors.groupingBy(item -> item.getCategoryId()));
        List<WheelCategoryDto> wheelCategoryDtos = new ArrayList<>();
        tasksByCategory.forEach((cat, list) -> wheelCategoryDtos.add(new WheelCategoryDto(cat.getTitle(), list.stream().mapToInt(item -> item.getEstimate()).sum(), cat.getColor())));
        return wheelCategoryDtos;
    }

}
