package ru.sfu.servlets;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.sfu.objects.StudyClass;
import ru.sfu.objects.Task;
import ru.sfu.objects.TodayScreen;
import ru.sfu.strategies.ExcludeAnnotationStrategy;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/day")
public class CalendarDayServlet extends HttpServlet {
    private final ExclusionStrategy strategy = new ExcludeAnnotationStrategy("Tasks");

    private final Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(strategy).create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = Arrays.asList(new Task(34, "Сделать что-то в заданное время", "12:00", "14:00", 0),
                new Task(35, "Сделать что-то в течение дня", null, null, 1),
                new Task(36, "Сделать x", "08:00", "09:00", 2));

        List<StudyClass> studyClasses = Arrays.asList(new StudyClass("Инженерия требований", "14:10 - 15:45", "Корпус 17, 4-16", "Офлайн"),
                new StudyClass("Серверное программирование", "15:55 - 17:30", "Корпус 17, 4-16", "Онлайн"));

        TodayScreen todayScreen = new TodayScreen(tasks,"2023-10-10", "X-XXX-XXX-XX-XX", studyClasses);

        String taskJsonString = this.gson.toJson(todayScreen);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(taskJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
