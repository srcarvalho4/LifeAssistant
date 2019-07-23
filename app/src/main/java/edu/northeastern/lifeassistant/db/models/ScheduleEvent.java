package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "schedule_events",
        foreignKeys = {
            @ForeignKey(entity = Activity.class,
                    parentColumns = "id",
                    childColumns = "activity_id",
                    onUpdate = CASCADE,
                    onDelete = CASCADE)
        },
        indices = {
            @Index(value="activity_id")
        })
public class ScheduleEvent {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @ColumnInfo(name = "start_time")
    private Calendar startTime;

    @ColumnInfo(name = "end_time")
    private Calendar endTime;

    @ColumnInfo(name = "days_of_week")
    private List<Integer> daysOfWeek;

    public ScheduleEvent(String activityId, Calendar startTime, Calendar endTime, List<Integer> daysOfWeek) {
        this.id = UUID.randomUUID().toString();
        this.activityId = activityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
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

}
