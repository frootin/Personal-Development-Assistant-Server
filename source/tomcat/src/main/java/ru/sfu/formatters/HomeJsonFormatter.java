package ru.sfu.formatters;

import org.modelmapper.ModelMapper;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.models.Event;
import ru.sfu.db.models.User;
import ru.sfu.db.services.DayNoteService;
import ru.sfu.db.services.EventService;
import ru.sfu.db.services.TaskService;
import ru.sfu.objects.*;
import ru.sfu.util.JsonUtil;

import java.time.LocalDate;
import java.util.List;


import static ru.sfu.util.JsonUtil.mapList;

public class HomeJsonFormatter {

    public static HomeDto getHomeDtoFromRepositories(TaskService taskService,
                                                     EventService eventService,
                                                     DayNoteService dayNoteService,
                                                     User curUser,
                                                     LocalDate centeredDate) {
        DayDto yesterday = getDayDtoFromRepositories(taskService, eventService, dayNoteService, curUser, centeredDate.minusDays(1));
        DayDto today = getDayDtoFromRepositories(taskService, eventService,dayNoteService,curUser, centeredDate);
        DayDto tomorrow = getDayDtoFromRepositories(taskService, eventService, dayNoteService, curUser, centeredDate.plusDays(1));
        List<TaskDto> freeTasks = mapList(taskService.getFreeTasks(curUser), TaskDto.class);
        List<TaskDto> lateTasks = mapList(taskService.getLateTasksForDate(curUser, centeredDate), TaskDto.class);
        List<TaskDto> soonTasks = mapList(taskService.getTasksOnDeadline(curUser, centeredDate), TaskDto.class);
        return new HomeDto(yesterday, today, tomorrow, freeTasks, lateTasks, soonTasks);
    }

    public static DayDto getDayDtoFromRepositories(TaskService taskService, EventService eventService, DayNoteService dayNoteService, User user, LocalDate date) {
        List<TaskDto> fixedTasks = mapList(taskService.getFixedTasksForDate(user, date), TaskDto.class);
        List<TaskDto> doneTasks = mapList(taskService.getDoneTasksForDate(user, date), TaskDto.class);
        List<EventDto> eventDtos = getEventsForDate(eventService, user, date);
        String dateAsString = date.toString();
        DayNote note = dayNoteService.findByDate(date);
        DayNoteDto textNote = null;
        if (note != null) {
            textNote = JsonUtil.ModelToDto(note, DayNoteDto.class);
        }
        return new DayDto(fixedTasks, doneTasks, dateAsString, textNote, eventDtos);
    }
   
    public static List<EventDto> getEventsForDate(EventService eventService, User user, LocalDate date) {
        List<Event> events = eventService.getEventsForDate(user, date);
        return mapList(events, EventDto.class);
    }
}
