package ru.sfu.db.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.User;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.userId = :user_id ORDER BY c.onWatch DESC, c.id")
    List<Category> findCategoriesByUserId(@Param("user_id") User user);
    @Query("SELECT c FROM Category c WHERE c.userId = :user_id AND c.onWatch = true")
    List<Category> findCategoriesByUserIdAndOnWatchIsTrue(@Param("user_id") User user);
    List<Category> findCategoriesByUserIdAndOnWatch(User user, Boolean onWatch, Sort sort);

    @Query("SELECT c FROM Category c WHERE c.userId = :user_id AND c.id IN :ids")
    List<Category> findCategoriesByUserIdAndListOfIds(@Param("user_id") User user, @Param("ids") List<Long> ids);
}
