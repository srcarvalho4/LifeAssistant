package edu.northeastern.lifeassistant.db.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "schedule_events",
        foreignKeys = {
            @ForeignKey(entity = ActivityDb.class,
                    parentColumns = "id",
                    childColumns = "activity_id",
                    onUpdate = CASCADE,
                    onDelete = CASCADE)
        },
        indices = {
            @Index(value="activity_id")
        })
public class ScheduleEventDb {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @ColumnInfo(name = "start_time")
    private Calendar startTime;

    @ColumnInfo(name = "end_time")
    private Calendar endTime;

    @ColumnInfo(name = "days_of_week")
    private List<Integer> daysOfWeek;

    @ColumnInfo(name = "alarm_ids")
    private List<Integer> alarmIds;

    @ColumnInfo(name = "reminder_switch_state")
    private Boolean reminderSwitchState;

    @ColumnInfo(name = "active")
    private Boolean isActive;

    public ScheduleEventDb(String activityId, String name, Calendar startTime, Calendar endTime,
                           List<Integer> daysOfWeek, Boolean reminderSwitchState) {
        this.id = UUID.randomUUID().toString();
        this.activityId = activityId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
        this.alarmIds = new ArrayList<>();
        this.reminderSwitchState = reminderSwitchState;
        this.isActive = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public List<Integer> getAlarmIds() {
        return alarmIds;
    }

    public void setAlarmIds(List<Integer> alarmIds) {
        //this.alarmIds = alarmIds;
        this.alarmIds = new ArrayList<>();
        if (alarmIds == null) return;
        Log.d("alarmIDs", "setting alarm in DB, length of input " + alarmIds.size());
        for (Integer i : alarmIds) {
            this.alarmIds.add(i);
        }
    }

    public Boolean getReminderSwitchState() {
        return reminderSwitchState;
    }

    public void setReminderSwitchState(Boolean reminderSwitchState) {
        this.reminderSwitchState = reminderSwitchState;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
