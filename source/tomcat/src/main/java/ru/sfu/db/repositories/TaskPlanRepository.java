package ru.sfu.db.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("DELETE FROM TaskPlan t WHERE t.task = :task AND t.plan = :plan")
    @Modifying
    @Transactional
    void deleteByTaskPlanId(@Param("task") Task task, @Param("plan") Plan plan);

    //@Override
    Optional<TaskPlan> findByPlanAndTask(Plan plan, Task task);
}
