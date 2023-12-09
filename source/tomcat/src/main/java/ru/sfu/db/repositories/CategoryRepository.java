package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
