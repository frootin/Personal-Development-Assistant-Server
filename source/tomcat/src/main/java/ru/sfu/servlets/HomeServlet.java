package ru.sfu.servlets;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.sfu.objects.StudyClass;
import ru.sfu.objects.Task;
import ru.sfu.objects.Home;
import ru.sfu.strategies.ExcludeAnnotationStrategy;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final ExclusionStrategy strategy = new ExcludeAnnotationStrategy("Tasks");

    private final Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(strategy).create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = Arrays.asList(
                new Task(34, "Полить цветы", "12:00", "14:00", 0),
                new Task(35, "Сдать лабораторную", null, null, 1),
                new Task(36, "Пробежка", "08:00", "09:00", 2),
                new Task(37, "Созвон с командой", "08:00", "09:00", 2),
                new Task(38, "Поработать над проектом", "08:00", null, 2),
                new Task(39, "Тренировка", "08:00", "09:00", 2),
                new Task(40, "Покормить коня", "08:00", "09:00", 2)
        );

        List<StudyClass> yesterdayStudyClasses = Arrays.asList(
                new StudyClass(
                        "Инженерия требований", "14:10", "15:45", "Корпус 17, 4-21", "офлайн"),
                new StudyClass(
                        "Серверное программирование", "15:55", "17:30", "Корпус 17, 4-07", "офлайн")
        );
        List<StudyClass> todayStudyClasses = Arrays.asList(
                new StudyClass(
                        "Профессионально-ориентированный иностранный язык", "10:15", "11:50", "-", "онлайн"),
                new StudyClass(
                        "Инженерия требований к программному обеспечению", "12:00", "13:35", "-", "онлайн"),
                new StudyClass(
                        "Теория систем", "14:10", "15:45", "-", "онлайн")
        );
        List<StudyClass> tomorrowStudyClasses = Arrays.asList(
                new StudyClass(
                        "Разработка корпоративных информационных систем", "12:00", "13:35", "Корпус 17, 4-20", "офлайн"),
                new StudyClass(
                        "Разработка корпоративных информационных систем", "14:10", "15:45", "Корпус 17, 4-20", "онлайн"),
                new StudyClass(
                        "Теория систем", "15:55", "17:30", "Корпус 17, 4-16", "офлайн"),
                new StudyClass(
                        "Информационная безопасность и защита информации", "17:40", "19:25", "Корпус 17, 5-07", "онлайн")
        );

        //вот тут меняешь на хом
        Home home = new Home(tasks,"2023-10-10", "Какая-то тут есть заметка. Но это все не важно, потому что " +
                "это просто проверка того, как будет располагаться и выглядеть текст",
                yesterdayStudyClasses, todayStudyClasses, tomorrowStudyClasses);

        String taskJsonString = this.gson.toJson(home);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(taskJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
