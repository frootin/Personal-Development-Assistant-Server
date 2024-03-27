package ru.sfu.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.User;
import ru.sfu.db.services.TaskService;
import ru.sfu.db.services.UserService;
import ru.sfu.formatters.DatetimeStringFormatter;
import ru.sfu.db.services.EventService;
import ru.sfu.formatters.HomeJsonFormatter;
import ru.sfu.objects.*;
import ru.sfu.util.JsonUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final UserService userRepository;

    @GetMapping
    public ScheduleDto getSchedule() {
        User curUser = userRepository.findById(1L);
        List<Event> events = eventService.getSchedule(curUser);
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setNumberOfWeeks(curUser.getSettings().getEventsTrackWeeksNum());
        Map<Integer, List<Event>> eventsByDay = events.stream().collect(Collectors.groupingBy(item -> item.getDayOfWeek()));
        List<DaySchedule> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<Integer, List<Event>> dayEventsByWeek = eventsByDay.getOrDefault(i, new ArrayList<Event>()).stream().collect(Collectors.groupingBy(item -> item.getWeekNum()));
            days.add(new DaySchedule(i, JsonUtil.mapList(dayEventsByWeek.get(1), EventDto.class), JsonUtil.mapList(dayEventsByWeek.get(2), EventDto.class)));
        }
        scheduleDto.setDays(days);
        return scheduleDto;
    }

}
