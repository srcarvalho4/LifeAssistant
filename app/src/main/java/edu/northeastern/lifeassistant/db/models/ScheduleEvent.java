package edu.northeastern.lifeassistant.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Set;

import edu.northeastern.lifeassistant.db.types.DayOfWeek;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "schedule_events",
        foreignKeys = {
                @ForeignKey(entity = Activity.class,
                        parentColumns = "id",
                        childColumns = "activity_id",
                        onUpdate = CASCADE,
                        onDelete = CASCADE)
        })
public class ScheduleEvent {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "activity_id")
    private Long activityId;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "end_time")
    private Date endTime;

    @ColumnInfo(name = "days_of_week")
    private Set<DayOfWeek> daysOfWeek;

    public ScheduleEvent(Long activityId, Date startTime, Date endTime, Set<DayOfWeek> daysOfWeek) {
        this.activityId = activityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

}
