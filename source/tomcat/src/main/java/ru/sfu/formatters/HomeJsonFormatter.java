package ru.sfu.formatters;

import com.fasterxml.jackson.databind.JsonNode;
import org.modelmapper.ModelMapper;
import ru.sfu.db.models.*;
import ru.sfu.db.services.*;
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
        List<EventDetailsDto> eventDtos = getEventsForDate(eventService, user, date);
        String dateAsString = date.toString();
        DayNote note = dayNoteService.findByDateOrBefore(user, date);
        DayNoteDto textNote = null;
        if (note != null) {
            textNote = JsonUtil.ModelToDto(note, DayNoteDto.class);
        }
        return new DayDto(fixedTasks, doneTasks, dateAsString, textNote, eventDtos);
    }
   
    public static List<EventDetailsDto> getEventsForDate(EventService eventService, User user, LocalDate date) {
        List<Event> events = eventService.getEventsForDate(user, date);
        return mapList(events, EventDetailsDto.class);
    }

    public static TaskWindowDto saveFromDetailedDto(TaskService taskService,
                                             PlanService planService,
                                             RepeatService repeatService,
                                             JsonNode json) {
        TaskWindowDto taskDto = JsonUtil.JsonToDto(json, TaskWindowDto.class);
        assert taskDto != null;
        Task task = JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class);
        assert task != null;
        if (taskDto.getRepeat() != null) {
            RepeatDto repeatDto = taskDto.getRepeat();
            Repeat repeat = new Repeat(task, repeatDto.getTerm(), repeatDto.getDays(), repeatDto.getRepeatStart(),
                    repeatDto.getRepeatEnd(), repeatDto.getNumberOfRepeats(), repeatDto.getRepeatInterval());
            repeat = repeatService.save(repeat);
            task.setRepeatId(repeat);
            taskService.createTasksForRepeat(repeat);
        }
        task = taskService.save(task);
        if (taskDto.getReferId() != null) {
            Plan plan = planService.findById(taskDto.getReferId());
            long step = planService.getNumberOfTasksInPLan(plan) + 1;
            taskService.addTaskToPlan(task, plan, step);
        }
        return JsonUtil.ModelToDto(task, TaskWindowDto.class);
    }

    public static TaskWindowDto updateFromDetailedDto(TaskService taskService,
                                                    PlanService planService,
                                                    RepeatService repeatService,
                                                    JsonNode json) {
        TaskWindowDto taskDto = JsonUtil.JsonToDto(json, TaskWindowDto.class);
        assert taskDto != null;
        Task task = JsonUtil.JsonToSingleModel(json, TaskWindowDto.class, Task.class);
        assert task != null;
        if (taskDto.getRepeat() != null) {
            RepeatDto repeatDto = taskDto.getRepeat();
            Repeat repeat = new Repeat(task, repeatDto.getTerm(), repeatDto.getDays(), repeatDto.getRepeatStart(),
                    repeatDto.getRepeatEnd(), repeatDto.getNumberOfRepeats(), repeatDto.getRepeatInterval());
            repeat = repeatService.save(repeat);
            task.setRepeatId(repeat);
            taskService.createTasksForRepeat(repeat);
        }
        task = taskService.save(task);
        if (taskDto.getReferId() != null) {
            Plan plan = planService.findById(taskDto.getReferId());
            long step = planService.getNumberOfTasksInPLan(plan) + 1;
            taskService.updateTaskPlan(task, plan, step);
        }
        /**if (taskDto.getRepeat() != null) {
         taskService.createTasksForRepeat(taskDto.getRepeat());
         }*/
        return JsonUtil.ModelToDto(task, TaskWindowDto.class);
    }
}
