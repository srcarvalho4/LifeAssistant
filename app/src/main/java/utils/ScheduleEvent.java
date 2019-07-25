package utils;

import android.graphics.Color;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class ScheduleEvent {

    Activity activityType;
    String name;
    Calendar startTime;
    Calendar endTime;
    //This is an arrayList of integers to correspond to days using the Calendar.MONDAY etc. mapping.
    ArrayList<Integer> days;

    public ScheduleEvent(Activity activityType, String name, Calendar startTime, Calendar endTime, ArrayList<Integer> days) {
        this.activityType = activityType;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    //Create a schedule event from a DB instance
    public ScheduleEvent(ScheduleEventDb event) {
        this(new Activity(event.getActivityId()), event.getId(), event.getStartTime(),
                event.getEndTime(), new ArrayList<>(event.getDaysOfWeek()));
    }

    //Edit all fields of schedule event
    public void edit(Activity activityType, String name, Calendar startTime, Calendar endTime, ArrayList<Integer> days) {
        this.activityType = activityType;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public Activity getActivityType() {
        return this.activityType;
    }

    public int getColor() {
        return this.activityType.color;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getDayData() {
        return days;
    }

}
