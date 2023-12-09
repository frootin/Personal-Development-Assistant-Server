package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sfu.annotations.Exclude;


public class StudyClass {
    private String name;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("stop_time")
    private String stopTime;
    private String place;
    private String format;

    public StudyClass(String name, String startTime, String stopTime, String place, String format) {
        this.name = name;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.place = place;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String time) {
        this.stopTime = stopTime;
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
