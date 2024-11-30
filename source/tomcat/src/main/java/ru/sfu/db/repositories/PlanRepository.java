package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT t FROM Plan t WHERE t.userId = :user_id ORDER BY t.startDate DESC")
    @EntityGraph(value = "Plan.tasks", type = EntityGraph.EntityGraphType.LOAD)
    List<Plan> findFullPlansByUserId(@Param("user_id") User userId);

    @Query("SELECT t FROM Plan t WHERE t.userId = :user_id AND t.status = :status ORDER BY t.startDate DESC")
    @EntityGraph(value = "Plan.tasks", type = EntityGraph.EntityGraphType.LOAD)
    List<Plan> findFullPlansByUserIdAndStatus(@Param("user_id") User userId, @Param("status") int status);
}
