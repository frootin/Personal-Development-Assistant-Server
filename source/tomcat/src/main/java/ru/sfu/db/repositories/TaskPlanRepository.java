package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.TaskPlan;
import ru.sfu.db.models.TaskPlanId;

@Repository
public interface TaskPlanRepository extends CrudRepository<TaskPlan, TaskPlanId>  {
    long countByPlan(Plan plan);
}
