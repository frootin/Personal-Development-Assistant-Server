package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.userId = :user_id AND (t.startDate = :start OR t.stopDate = :stop)")
    List<Task> findTaskByUserIdAndStartDateOrStopDate(@Param("user_id") User userId,
                                                      @Param("start") LocalDate taskStart,
                                                      @Param("stop") LocalDate taskStop);
    List<Task> findTaskByUserIdAndDoneByBetweenAndStartDateIsNot(User user, LocalDateTime dayStart, LocalDateTime dayEnd, LocalDate taskStart);
    List<Task> findTaskByUserIdAndStopDateIsNullAndStatusIs(User user, int status);
    List<Task> findTaskByUserIdAndDoneByIsNullAndStopDateLessThan(User user, LocalDate today);
    List<Task> findTaskByUserIdAndStartDateIsLessThanAndStopDateIsGreaterThan(User user, LocalDate today, LocalDate stopToday);
    List<Task> findTaskByUserId(User user);
    List<Task> findTaskByCategoryIdAndDoneByBetween(Category category, LocalDateTime dayStart, LocalDateTime dayEnd);
    List<Task> findTaskByUserIdAndDoneByBetween(User user, LocalDateTime dayStart, LocalDateTime dayEnd);
    @Query("SELECT t FROM Task t WHERE t.userId = :user_id AND t.doneBy BETWEEN :start AND :stop AND t.categoryId IN :categories")
    List<Task> findTaskByUserIdAndDoneByBetweenAndInCategories(@Param("user_id") User user,
                                                               @Param("start") LocalDateTime dayStart,
                                                               @Param("stop") LocalDateTime dayEnd,
                                                               @Param("categories") List<Category> categories);
}
