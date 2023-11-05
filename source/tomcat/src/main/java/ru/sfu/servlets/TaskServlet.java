package ru.sfu.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import ru.sfu.objects.Task;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Task task = new Task(1, "Сделать что-то", "Прийти по адресу...", "10 окт. 2023", "10 окт. 2023",
                "12:25","12:55", "Учёба", "#32a852", 1, "Не повторяется", 0);
        String taskJsonString = this.gson.toJson(task);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(taskJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
