package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.PlanRepository;

import java.util.List;

@Service
public class PlanService {
    private PlanRepository repository;

    @Autowired
    public PlanService(PlanRepository repository) {
        this.repository = repository;
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
        return repository.findPlansByUserId(user);
    }
}
