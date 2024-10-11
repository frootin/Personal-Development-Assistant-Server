package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.repositories.DayNoteRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DayNoteService {
    private DayNoteRepository repository;
    @Autowired
    public DayNoteService(DayNoteRepository repository) {
        this.repository = repository;
    }

    public DayNote save(DayNote note) {
        return repository.save(note);
    }

    public DayNote findByDate(LocalDate date) {
        List<DayNote> note = repository.findDayNoteByDay(date);
        if (note.isEmpty()) return null;
        return note.get(0);
    }
}
