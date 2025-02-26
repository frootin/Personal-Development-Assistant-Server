package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.db.models.Plan;
import ru.sfu.db.models.Task;
import ru.sfu.db.models.TaskPlan;
import ru.sfu.db.models.TaskPlanId;

import java.util.Optional;

@Repository
public interface TaskPlanRepository extends CrudRepository<TaskPlan, TaskPlanId>  {
    long countByPlan(Plan plan);

    @Transactional
    void deleteByPlanAndTask(Plan plan, Task task);

    //@Override
    Optional<TaskPlan> findByPlanAndTask(Plan plan, Task task);
}
