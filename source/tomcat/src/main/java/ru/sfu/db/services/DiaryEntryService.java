package ru.sfu.db.services;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.DiaryEntry;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.DiaryEntryRepository;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
public class DiaryEntryService {
    private DiaryEntryRepository repository;

    @Autowired
    public DiaryEntryService(DiaryEntryRepository repository) {
        this.repository = repository;
    }

    public DiaryEntry save(DiaryEntry diaryEntry) {
        return repository.save(diaryEntry);
    }

    public DiaryEntry updateOrInsert(DiaryEntry diaryEntry) {
        if (diaryEntry.getText().isBlank()) {
            repository.deleteById(diaryEntry.getId());
            return null;
        }
        return repository.updateOrInsert(diaryEntry);
    }

    public DiaryEntry findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(DiaryEntry diaryEntry) {
        repository.delete(diaryEntry);
    }

    public List<DiaryEntry> getAllEntries(User user) {
        return repository.findEntriesByUserId(user, Sort.by(Sort.Direction.DESC, "day"));
    }
}
