package ru.sfu.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ru.sfu.annotations.Exclude;

import java.util.List;

public class Home {
    @Expose
    @SerializedName("today_tasks")
    private List<Task> todayTasks;
    @SerializedName("free_tasks")
    private List<Task> freeTasks;
    @SerializedName("late_tasks")
    private List<Task> lateTasks;
    private String day;
    @SerializedName("text_note")
    private String textNote;
    @SerializedName("classes")
    private List<StudyClass> studyClasses;
    private List<Task> tasks;

    public Home(List<Task> todayTasks, List<Task> freeTasks, List<Task> lateTasks, String day, String textNote, List<StudyClass> studyClasses) {
        this.todayTasks = todayTasks;
        this.freeTasks = freeTasks;
        this.lateTasks = lateTasks;
        this.day = day;
        this.textNote = textNote;
        this.studyClasses = studyClasses;
    }

    public Home(List<Task> tasks,String day, String textNote, List<StudyClass> studyClasses) {
        this.tasks = tasks;
        this.day = day;
        this.textNote = textNote;
        this.studyClasses = studyClasses;
    }

    public List<Task> getTodayTasks() {
        return todayTasks;
    }

    public void setTodayTasks(List<Task> todayTasks) {
        this.todayTasks = todayTasks;
    }

    public List<Task> getFreeTasks() {
        return freeTasks;
    }

    public void setFreeTasks(List<Task> freeTasks) {
        this.freeTasks = freeTasks;
    }

    public List<Task> getLateTasks() {
        return lateTasks;
    }

    public void setLateTasks(List<Task> lateTasks) {
        this.lateTasks = lateTasks;
    }

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

    public List<StudyClass> getStudyClasses() {
        return studyClasses;
    }

    public void setStudyClasses(List<StudyClass> studyClasses) {
        this.studyClasses = studyClasses;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
