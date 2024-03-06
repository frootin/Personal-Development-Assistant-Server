package ru.sfu.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.EventRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void save(Event event) {
        repository.save(event);
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
        return repository.findEventsByUserIdAndDayOfWeekAndWeekNum(user, date.getDayOfWeek().getValue(), 1);
    }
}
