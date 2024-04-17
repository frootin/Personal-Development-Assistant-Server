package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.User;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findCategoriesByUserId(User user);
    @Query("SELECT c FROM Category c WHERE c.userId = :user_id AND c.onWatch = true")
    List<Category> findCategoriesByUserIdAndOnWatchIsTrue(@Param("user_id") User user);
}
