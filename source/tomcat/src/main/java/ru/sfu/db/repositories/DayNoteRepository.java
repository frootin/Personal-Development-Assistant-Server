package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.DayNote;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayNoteRepository extends CrudRepository<DayNote, Long> {
    List<DayNote> findDayNoteByDay(LocalDate day);
}
