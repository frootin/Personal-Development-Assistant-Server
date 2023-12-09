package ru.sfu.formatters;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class DatetimeStringFormatter {
    public static LocalDateTime getDateFromRequest(HttpServletRequest request) {
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        LocalDate date = LocalDate.of(year, month, day);
        return date.atStartOfDay();
    }

    public static LocalDate getDateFromYearMonthDay(String yearStr, String monthStr, String dayStr) {
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);
        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }
}
