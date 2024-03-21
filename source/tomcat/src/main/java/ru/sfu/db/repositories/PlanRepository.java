package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
    @Override
    @NonNull
    @EntityGraph(value = "Plan.tasks", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Plan> findById(@NonNull Long id);

    List<Plan> findPlansByUserId(User user);
}
