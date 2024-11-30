package ru.sfu.db.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.db.models.Category;
import ru.sfu.db.models.DiaryEntry;
import ru.sfu.db.models.User;

import java.util.List;

@Repository
public interface DiaryEntryRepository extends CrudRepository<DiaryEntry, Long> {
    List<DiaryEntry> findEntriesByUserId(User user, Sort sort);

    @Transactional
    default DiaryEntry updateOrInsert(DiaryEntry entry) {
        return save(entry);
    }
}
