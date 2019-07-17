package utils;

import java.time.LocalTime;
import java.util.ArrayList;

public class ScheduleEvent {

    Activity activityType;
    String name;
    LocalTime startTime;
    LocalTime endTime;
    ArrayList<Day> days;

    public ScheduleEvent(Activity activityType, String name, LocalTime startTime, LocalTime endTime, ArrayList<Day> days) {
        this.activityType = activityType;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    //Edit all fields of schedule event
    public void edit(Activity activityType, String name, LocalTime startTime, LocalTime endTime, ArrayList<Day> days) {
        this.activityType = activityType;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }
}
