package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.User;
import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
    List<Plan> findPlansByUserId(User user);
}
