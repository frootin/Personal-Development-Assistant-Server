package ru.sfu.db.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.db.models.Bookmark;
import ru.sfu.db.models.DiaryEntry;
import ru.sfu.db.models.User;

import java.util.List;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
    List<Bookmark> findEntriesByUserId(User user, Sort sort);

    @Transactional
    default Bookmark updateOrInsert(Bookmark entry) {
        return save(entry);
    }

    @Query("SELECT b FROM Bookmark b WHERE :tag IN b.tags")
    List<Bookmark> findByTag(String tag);
}
