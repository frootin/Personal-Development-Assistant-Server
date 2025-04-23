package ru.sfu.db.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.db.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.userId = :user_id AND (t.startDate = :start OR t.stopDate = :stop)")
    List<Task> findTaskByUserIdAndStartDateOrStopDate(@Param("user_id") User userId,
                                                      @Param("start") LocalDate taskStart,
                                                      @Param("stop") LocalDate taskStop);
    List<Task> findTaskByUserIdAndDoneByTmzBetweenAndStartDateIsNot(User user, LocalDateTime dayStart, LocalDateTime dayEnd, LocalDate taskStart);
    @Query(
            value = "SELECT t FROM Task t WHERE t.userId = :user_id AND t.status = 1 AND t.doneByTmz BETWEEN :dayStart AND :dayEnd AND ((t.startDate <> :date AND t.stopDate <> :date) OR (t.startDate IS NULL AND t.stopDate <> :date) OR (t.startDate <> :date AND t.stopDate IS NULL) OR (t.startDate IS NULL AND t.stopDate IS NULL))"
    )
    List<Task> findTaskByUserIdAndDoneByTmzBetweenAndDateIsNot(@Param("user_id") User user,
                                                               @Param("dayStart") LocalDateTime dayStart,
                                                               @Param("dayEnd") LocalDateTime dayEnd,
                                                               @Param("date") LocalDate date);
    List<Task> findTaskByUserIdAndDoneByTmzBetweenAndStatus(User user, LocalDateTime dayStart, LocalDateTime dayEnd, int status);
    List<Task> findTaskByUserIdAndStopDateIsNullAndStatusIs(User user, int status);
    List<Task> findTaskByUserIdAndStatusAndStopDateLessThan(User user, int status, LocalDate today);
    List<Task> findTaskByUserIdAndStartDateIsLessThanAndStopDateIsGreaterThanAndStatus(User user, LocalDate today, LocalDate stopToday, int status);
    List<Task> findTaskByUserId(User user);
    List<Task> findTaskByCategoryIdAndDoneByTmzBetweenAndStatus(Category category, LocalDateTime dayStart, LocalDateTime dayEnd, int status);
    List<Task> findTaskByUserIdAndDoneByTmzBetweenAndStatus(User user, LocalDateTime dayStart, LocalDateTime dayEnd, int status, Sort sort);
    @Query("SELECT t FROM Task t WHERE t.userId = :user_id AND t.status = 1 AND t.doneByTmz BETWEEN :start AND :stop AND t.categoryId IN :categories")
    List<Task> findTaskByUserIdAndDoneByTmzBetweenAndInCategories(@Param("user_id") User user,
                                                                  @Param("start") LocalDateTime dayStart,
                                                                  @Param("stop") LocalDateTime dayEnd,
                                                                  @Param("categories") List<Category> categories);

    @Query("SELECT t FROM Task t JOIN t.plan p WHERE p.plan = :plan ORDER BY p.stepNumber")
    List<Task> findTaskByPlan(@Param("plan") Plan plan);
    Long deleteByRepeatId_Id(Long repeatIdId);
    @Transactional
    void deleteByRepeatId(Repeat repeatId);
    Long deleteByRepeatId_IdAndStartDateIsGreaterThan(Long repeatIdId, LocalDate startDate);
    @Transactional
    void deleteByRepeatIdAndStartDateIsGreaterThanEqual(Repeat repeatId, LocalDate startDate);
}
