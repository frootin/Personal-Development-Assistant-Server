package ru.sfu.db.services;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.DayNoteRepository;
import ru.sfu.objects.StorageSearchDto;
import ru.sfu.util.JsonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DayNoteService {
    @PersistenceContext
    private EntityManager entityManager;
    private DayNoteRepository repository;
    @Autowired
    public DayNoteService(DayNoteRepository repository) {
        this.repository = repository;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public DayNote save(DayNote note) {
        return repository.save(note);
    }

    public DayNote findByDate(LocalDate date) {
        List<DayNote> note = repository.findDayNoteByDay(date);
        if (note.isEmpty()) return null;
        return note.get(0);
    }

    public DayNote findByDateOrBefore(User user, LocalDate date) {
        DayNote note = JsonUtil.getOrDefault(0, null, repository.findFirstByOrderByDayDescByUserIdAndDayLessThanEqual(user, date));
        return note;
    }

    public List<DayNote> filterForStorage(User user, StorageSearchDto searchDto) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<DayNote> cq = session.getCriteriaBuilder().createQuery(DayNote.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<DayNote> dayNote = cq.from(DayNote.class);
        predicates.add(criteriaBuilder.equal(dayNote.get("userId"), user));

        if (searchDto.getText() != null) {
            //predicates.add(criteriaBuilder.like(task.get("name"), "%" + text + "%"));
            predicates.add(criteriaBuilder.like(dayNote.get("text"), "%" + searchDto.getText() + "%"));
        }

        if (searchDto.getStartDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(dayNote.get("day"), searchDto.getStartDate()));
        }

        if (searchDto.getStopDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(dayNote.get("day"), searchDto.getStopDate()));
        }

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }
}
