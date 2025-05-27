package ru.sfu.db.services;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Bookmark;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.BookmarkRepository;
import ru.sfu.exceptions.EmptyBookmarkException;
import ru.sfu.objects.StorageSearchDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkService {
    @PersistenceContext
    private EntityManager entityManager;
    private BookmarkRepository repository;

    @Autowired
    public BookmarkService(BookmarkRepository repository) {
        this.repository = repository;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public Bookmark save(Bookmark bookmark) throws EmptyBookmarkException {
        if (bookmark.getName().isBlank()) throw new EmptyBookmarkException();
        return repository.save(bookmark);
    }

    public Bookmark updateOrInsert(Bookmark bookmark) {
        if (bookmark.getDetails().isBlank()) {
            repository.deleteById(bookmark.getId());
            return null;
        }
        return repository.updateOrInsert(bookmark);
    }

    public Bookmark findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Bookmark bookmark) {
        repository.delete(bookmark);
    }

    public List<Bookmark> getAllEntries(User user) {
        return repository.findEntriesByUserId(user, Sort.by(Sort.Direction.DESC, "day"));
    }

    public List<Bookmark> filterForStorage(User user, StorageSearchDto searchDto) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<Bookmark> cq = session.getCriteriaBuilder().createQuery(Bookmark.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Bookmark> bookmark = cq.from(Bookmark.class);
        predicates.add(criteriaBuilder.equal(bookmark.get("userId"), user));

        if (searchDto.getText() != null) {
            predicates.add(criteriaBuilder.like(bookmark.get("name"), "%" + bookmark + "%"));
            predicates.add(criteriaBuilder.like(bookmark.get("details"), "%" + searchDto.getText() + "%"));
        }

        /**if (searchDto.getStartDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(bookmark.get("day"), searchDto.getStartDate()));
        }

        if (searchDto.getStopDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(bookmark.get("day"), searchDto.getStopDate()));
        }*/

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }
}
