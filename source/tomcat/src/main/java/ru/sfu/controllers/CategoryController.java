package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Category;
import ru.sfu.db.services.CatService;
import ru.sfu.db.services.UserService;
import ru.sfu.objects.CategoryDto;
import ru.sfu.util.JsonUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    UserService userService;
    CatService categoryService;

    @PostMapping
    public CategoryDto createCategory(@RequestBody JsonNode json) {
        Category category = JsonUtil.JsonToSingleModel(json, CategoryDto.class, Category.class);
        assert category != null;
        return JsonUtil.ModelToDto(categoryService.save(category), CategoryDto.class);
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryService.getCategoriesForUser(userService.findById(1L));
        return JsonUtil.mapList(categories, CategoryDto.class);
    }

    @GetMapping("/active")
    public List<CategoryDto> getActiveCategories() {
        List<Category> categories = categoryService.getActiveCategoriesForUser(userService.findById(1L));
        return JsonUtil.mapList(categories, CategoryDto.class);
    }

    @PutMapping
    public CategoryDto updateCategory(@RequestBody JsonNode json) {
        Category category = JsonUtil.JsonToSingleModel(json, CategoryDto.class, Category.class);
        assert category != null;
        return JsonUtil.ModelToDto(categoryService.save(category), CategoryDto.class);
    }
}
