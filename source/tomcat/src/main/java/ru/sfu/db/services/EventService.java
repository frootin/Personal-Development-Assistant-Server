package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.EventRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class EventService {
    private EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event save(Event event) {
        return repository.save(event);
    }

    public Event findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.delete(findById(id));
    }

    public List<Event> getAllEventsForUser(User user) {
        return repository.findEventsByUserId(user);
    }

    public List<Event> getEventsForDate(User user, LocalDate date) {
        LocalDate eventsTrackStartDate = user.getSettings().getEventsTrackStartDate();
        LocalDate nextStartOfWeek = eventsTrackStartDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        long daysUntilNextWeek = ChronoUnit.DAYS.between(eventsTrackStartDate, nextStartOfWeek);
        long daysUntilDate = ChronoUnit.DAYS.between(eventsTrackStartDate, date);
        long weekNum = 1;
        if (daysUntilDate > daysUntilNextWeek) {
            weekNum = (((daysUntilDate - daysUntilNextWeek) / 7 + 1) % user.getSettings().getEventsTrackWeeksNum()) + 1;
        }
        return repository.findEventsByUserIdAndDayOfWeekAndWeekNum(user, date.getDayOfWeek().getValue() - 1, (int) weekNum);
    }

    public List<Event> getSchedule(User user) {
        return repository.findEventsByUserId(user);
    }
}
