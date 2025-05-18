package ru.sfu.db.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.models.DiaryEntry;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.DiaryEntryRepository;
import ru.sfu.exceptions.EmptyDiaryEntryException;
import ru.sfu.objects.StorageSearchDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryEntryService {
    @PersistenceContext
    private EntityManager entityManager;
    private DiaryEntryRepository repository;

    @Autowired
    public DiaryEntryService(DiaryEntryRepository repository) {
        this.repository = repository;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public DiaryEntry save(DiaryEntry diaryEntry) throws EmptyDiaryEntryException  {
        if (diaryEntry.getText().isBlank()) throw new EmptyDiaryEntryException();
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

    public List<DiaryEntry> filterForStorage(User user, StorageSearchDto searchDto) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<DiaryEntry> cq = session.getCriteriaBuilder().createQuery(DiaryEntry.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<DiaryEntry> diaryEntry = cq.from(DiaryEntry.class);
        predicates.add(criteriaBuilder.equal(diaryEntry.get("userId"), user));

        if (searchDto.getText() != null) {
            //predicates.add(criteriaBuilder.like(task.get("name"), "%" + text + "%"));
            predicates.add(criteriaBuilder.like(diaryEntry.get("text"), "%" + searchDto.getText() + "%"));
        }

        if (searchDto.getStartDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(diaryEntry.get("day"), searchDto.getStartDate()));
        }

        if (searchDto.getStopDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(diaryEntry.get("day"), searchDto.getStopDate()));
        }

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }
}
