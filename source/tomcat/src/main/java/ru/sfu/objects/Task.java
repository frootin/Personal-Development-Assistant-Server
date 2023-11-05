package ru.sfu.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ru.sfu.annotations.Exclude;

public class Task {
    private long id;
    private String name;
    @Exclude
    private String description;
    @Exclude
    @SerializedName("start_date")
    private String startDate;
    @Exclude
    @SerializedName("stop_date")
    private String stopDate;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("stop_time")
    private String stopTime;
    @Exclude
    @SerializedName("category_name")
    private String categoryName;
    @Exclude
    @SerializedName("category_color")
    private String categoryColor;
    @Exclude
    private int estimate;
    @Exclude
    private String repeat;
    private int status;

    public long getId() {
        return id;
    }

    public Task(long id, String name, String startTime, String stopTime, int status) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.status = status;
    }

    public Task(long id, String name, String description, String startDate, String stopDate, String startTime, String stopTime, String categoryName, String categoryColor, int estimate, String repeat, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.estimate = estimate;
        this.repeat = repeat;
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public int getEstimate() {
        return estimate;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
