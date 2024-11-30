package ru.sfu.formatters;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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

    public static LocalDate getClosestExistingMonthlyDate(LocalDate baseDate, int day) {
        LocalDate newDate = LocalDate.of(baseDate.getYear(), baseDate.getMonth(), 1);;
        try {
            newDate = LocalDate.of(baseDate.getYear(), baseDate.getMonth(), day);
        } catch (DateTimeException e) {
            newDate = newDate.with(TemporalAdjusters.lastDayOfMonth());
        }
        return newDate;
    }
}
