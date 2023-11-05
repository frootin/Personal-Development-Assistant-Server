package ru.sfu.objects;

public class StudyClass {
    private String name;
    private String time;
    private String place;
    private String format;

    public StudyClass(String name, String time, String place, String format) {
        this.name = name;
        this.time = time;
        this.place = place;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
