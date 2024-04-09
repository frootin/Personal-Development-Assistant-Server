package ru.sfu.db.services;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.PlanRepository;
import ru.sfu.db.repositories.TaskPlanRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

//@Transactional
//@AllArgsConstructor
@Service
public class PlanService {
    @PersistenceContext
    private EntityManager entityManager;
    private PlanRepository repository;
    private TaskPlanRepository taskPlanRepository;

    @Autowired
    public PlanService(PlanRepository repository, TaskPlanRepository taskPlanRepository) {
        this.repository = repository;
        this.taskPlanRepository = taskPlanRepository;
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void save(Plan plan) {
        repository.save(plan);
    }

    public Plan findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.delete(findById(id));
    }

    public List<Plan> getPlansForUser(User user) {
        /**Session session = getSession();
        CriteriaQuery<Plan> cq = session.getCriteriaBuilder().createQuery(Plan.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        Root<Plan> plan = cq.from(Plan.class);
        Predicate finalPredicate = criteriaBuilder.equal(plan.get("userId"), user);
        return session.createQuery(cq.where(finalPredicate)).getResultList();*/
        return repository.findPlansByUserId(user);
    }

    public long getNumberOfTasksInPLan(Plan plan) {
        return taskPlanRepository.countByPlan(plan);
    }
}
