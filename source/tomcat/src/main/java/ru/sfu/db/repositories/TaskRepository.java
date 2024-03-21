package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findTaskByUserIdAndStartDate(User user, LocalDate taskStart);
    List<Task> findTaskByUserIdAndDoneByBetweenAndStartDateIsNot(User user, LocalDateTime dayStart, LocalDateTime dayEnd, LocalDate tasStart);
    List<Task> findTaskByUserIdAndStartDateIsNullAndStopDateIsNull(User user);
    List<Task> findTaskByUserIdAndDoneByIsNullAndStopDateLessThan(User user, LocalDate today);
    List<Task> findTaskByUserIdAndStartDateIsLessThanAndStopDateIsGreaterThan(User user, LocalDate today, LocalDate stopToday);
    List<Task> findTaskByUserId(User user);
}
