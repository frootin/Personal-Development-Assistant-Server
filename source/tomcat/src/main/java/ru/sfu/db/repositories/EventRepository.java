package ru.sfu.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.User;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findEventsByUserId(User user);
    List<Event> findEventsByUserIdAndDayOfWeekAndWeekNum(User user, int dayOfWeek, int weekNum);
}
