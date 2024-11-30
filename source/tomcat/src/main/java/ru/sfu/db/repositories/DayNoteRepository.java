package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DayNoteRepository extends CrudRepository<DayNote, Long> {
    List<DayNote> findDayNoteByDay(LocalDate day);

    @Query("SELECT n FROM DayNote n WHERE n.userId = :user AND n.day <= :day ORDER BY n.day DESC ")
    List<DayNote> findFirstByOrderByDayDescByUserIdAndDayLessThanEqual(@Param("user") User user, @Param("day") LocalDate day);
}
