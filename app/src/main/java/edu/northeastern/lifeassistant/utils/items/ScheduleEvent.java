package edu.northeastern.lifeassistant.utils.items;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class ScheduleEvent {

    Activity activityType;
    String name;
    Calendar startTime;
    Calendar endTime;
    //This is an arrayList of integers to correspond to days using the Calendar.MONDAY etc. mapping.
    ArrayList<Integer> days;
    String startText = null;
    String endText = null;

    //construction directly using data
    public ScheduleEvent(Activity activityType, String name, Calendar startTime, Calendar endTime, ArrayList<Integer> days) {
        this.activityType = activityType;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    //Create a schedule event from a DB instance
    public ScheduleEvent(Context applicationContext, String eventId) {
        ScheduleEventDb event = AppDatabase.getAppDatabase(applicationContext).scheduleEventDao().findScheduleEventById(eventId);
        ActivityDb activityDb = AppDatabase.getAppDatabase(applicationContext).activityDao().findActivityById(event.getActivityId());
        this.activityType = new Activity(applicationContext, event.getActivityId());
        this.name = event.getName();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.days = new ArrayList<>(event.getDaysOfWeek());
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

    public String getStartTimeText() {
        if(startText == null) {
            parseTimes();
        }
        return startText;
    }

    public String getEndTimeText() {
        if(endText == null) {
            parseTimes();
        }
        return endText;
    }

    //function to format time value into a clock style
    private void parseTimes() {
        startText = "";

        if(this.startTime.get(Calendar.HOUR) == 0) {
            startText += "12";
        }
        else {
            startText += this.startTime.get(Calendar.HOUR);
        }
        startText += ":";
        if (this.startTime.get(Calendar.MINUTE) < 10) {
            startText += "0";
        }
        startText += this.startTime.get(Calendar.MINUTE);
        if (this.startTime.get(Calendar.AM_PM) == 0) {
            startText += "am";
        }
        else {
            startText += "pm";
        }


        endText = "";

        if(this.endTime.get(Calendar.HOUR) == 0) {
            endText += "12";
        }
        else {
            endText += this.endTime.get(Calendar.HOUR);
        }
        endText += ":";
        if (this.endTime.get(Calendar.MINUTE) < 10) {
            endText += "0";
        }
        endText += this.endTime.get(Calendar.MINUTE);
        if (this.endTime.get(Calendar.AM_PM) == 0) {
            endText += "am";
        }
        else {
            endText += "pm";
        }
    }
}
