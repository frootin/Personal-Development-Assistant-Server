package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.CategoryRepository;

import java.util.List;

@Service
public class CatService {
    private CategoryRepository repository;
    
    @Autowired
    public CatService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category save(Category category) {
        return repository.save(category);
    }

    public Category findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Category> getCategoriesForUser(User user) {
        return repository.findCategoriesByUserId(user);
    }

    public List<Category> getActiveCategoriesForUser(User user) {
        return repository.findCategoriesByUserIdAndOnWatchIsTrue(user);
    }
}
