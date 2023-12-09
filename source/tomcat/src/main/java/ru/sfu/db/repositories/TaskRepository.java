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
    List<Task> findTaskByUserIdAndTaskStart(User user, LocalDate taskStart);
    List<Task> findTaskByUserIdAndDoneByBetween(User user, LocalDateTime dayStart, LocalDateTime dayEnd);
    List<Task> findTaskByUserIdAndTaskStartIsNullAndTaskStopIsNull(User user);
    List<Task> findTaskByUserIdAndDoneByIsNullAndTaskStopLessThan(User user, LocalDate today);
}
