package ru.sfu.db.services;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.TaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @PersistenceContext
    private EntityManager entityManager;

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void save(Task task) {
        repository.save(task);
    }

    public Task findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.delete(findById(id));
    }

    public List<Task> filterByFields(User user, String name, String details, Integer status) {
        List<Predicate> predicates = new ArrayList<>();
        Session session = getSession();
        CriteriaQuery<Task> cq = session.getCriteriaBuilder().createQuery(Task.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Task> task = cq.from(Task.class);
        predicates.add(criteriaBuilder.equal(task.get("userId"), user));

        if (name != null) {
            predicates.add(criteriaBuilder.like(task.get("name"), "%" + name + "%"));
        }

        if (details != null) {
            predicates.add(criteriaBuilder.like(task.get("details"), "%" + details + "%"));
        }

        if (status != null) {
            predicates.add(criteriaBuilder.equal(task.get("status"), status));
        }
        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        return session.createQuery(cq.where(finalPredicate)).getResultList();
    }
}
