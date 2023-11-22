package ru.sfu.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ru.sfu.annotations.Exclude;

import java.util.List;

public class Home {
    @Expose
    @SerializedName("tasks")
    private List<Task> tasks;
    private String day;
    @SerializedName("text_note")
    private String textNote;
    @SerializedName("yesterday_classes")
    private List<StudyClass> yesterdayStudyClasses;
    @SerializedName("today_classes")
    private List<StudyClass> todayStudyClasses;
    @SerializedName("tomorrow_classes")
    private List<StudyClass> tomorrowStudyClasses;

    public Home(List<Task> tasks, String day, String textNote, List<StudyClass> yesterdayStudyClasses, List<StudyClass> todayStudyClasses, List<StudyClass> tomorrowStudyClasses) {
        this.tasks = tasks;
        this.day = day;
        this.textNote = textNote;
        this.yesterdayStudyClasses = yesterdayStudyClasses;
        this.todayStudyClasses = todayStudyClasses;
        this.tomorrowStudyClasses = tomorrowStudyClasses;
    }

//    public Home(List<Task> tasks, String day, String textNote, List<StudyClass> yesterdayStudyClasses, List<StudyClass> todayStudyClasses, List<StudyClass> tomorrowStudyClasses) {
//        this.tasks = tasks;
//        this.day = day;
//        this.textNote = textNote;
//        this.yesterdayStudyClasses = yesterdayStudyClasses;
//        this.todayStudyClasses = todayStudyClasses;
//        this.tomorrowStudyClasses = tomorrowStudyClasses;
//    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTextNote() {
        return textNote;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }

    public List<StudyClass> getTodayStudyClasses() {
        return todayStudyClasses;
    }

    public void setTodayStudyClasses(List<StudyClass> todayStudyClasses) {
        this.todayStudyClasses = todayStudyClasses;
    }

    public List<StudyClass> getYesterdayStudyClasses() {
        return yesterdayStudyClasses;
    }

    public void setYesterdayStudyClasses(List<StudyClass> yesterdayStudyClasses) {
        this.yesterdayStudyClasses = yesterdayStudyClasses;
    }
    public List<StudyClass> getTomorrowStudyClasses() {
        return tomorrowStudyClasses;
    }

    public void setTomorrowStudyClasses(List<StudyClass> tomorrowStudyClasses) {
        this.tomorrowStudyClasses = tomorrowStudyClasses;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
