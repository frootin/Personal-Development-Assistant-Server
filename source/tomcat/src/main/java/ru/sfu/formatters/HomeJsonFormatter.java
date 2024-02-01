package ru.sfu.formatters;

import org.modelmapper.ModelMapper;
import ru.sfu.db.models.User;
import ru.sfu.db.repositories.TaskRepository;
import ru.sfu.objects.DayDto;
import ru.sfu.objects.HomeDto;
import ru.sfu.objects.StudyClass;
import ru.sfu.objects.TaskDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeJsonFormatter {
    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static HomeDto getHomeDtoFromRepositories(TaskRepository taskRepository, User curUser, LocalDate centeredDate) {
        DayDto yesterday = getDayDtoFromRepositories(taskRepository, curUser, centeredDate.minusDays(1));
        DayDto today = getDayDtoFromRepositories(taskRepository, curUser, centeredDate);
        DayDto tomorrow = getDayDtoFromRepositories(taskRepository, curUser, centeredDate.plusDays(1));
        List<TaskDto> freeTasks = mapList(taskRepository.findTaskByUserIdAndStartDateIsNullAndStopDateIsNull(curUser), TaskDto.class);
        List<TaskDto> lateTasks = mapList(taskRepository.findTaskByUserIdAndDoneByIsNullAndStopDateLessThan(curUser, centeredDate), TaskDto.class);
        return new HomeDto(yesterday, today, tomorrow, freeTasks, lateTasks);
    }

    public static DayDto getDayDtoFromRepositories(TaskRepository taskRepository, User user, LocalDate date) {
        List<TaskDto> fixedTasks = mapList(taskRepository.findTaskByUserIdAndStartDate(user, date), TaskDto.class);
        List<TaskDto> doneTasks = mapList(
                taskRepository.findTaskByUserIdAndDoneByBetween(user, date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX)), TaskDto.class);
        List<StudyClass> studyClasses = Arrays.asList(new StudyClass("Инженерия требований", "14:10","15:45", "Корпус 17, 4-16", "Офлайн"),
                new StudyClass("Серверное программирование", "15:55","17:30", "Корпус 17, 4-16", "Онлайн"));
        String dateAsString = date.toString();
        String textNote = "X-XXX-XXX-XX-XX";
        return new DayDto(fixedTasks, doneTasks, dateAsString, textNote, studyClasses);
    }
}
