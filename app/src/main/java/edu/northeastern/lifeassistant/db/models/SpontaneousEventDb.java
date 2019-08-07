package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Calendar;
import java.util.UUID;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "spontaneous_events",
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
public class SpontaneousEventDb {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @ColumnInfo(name = "start_time")
    private Calendar startTime;

    @ColumnInfo(name = "end_time")
    private Calendar endTime;

    @ColumnInfo(name = "active")
    private Boolean isActive;

    @ColumnInfo(name = "start_value")
    private String startValue;

    @ColumnInfo(name = "end_value")
    private String endValue;

    @ColumnInfo(name = "final_value")
    private String finalValue;

    public SpontaneousEventDb(String activityId, Calendar endTime, String startValue, String endValue,
                              String finalValue) {
        this.id = UUID.randomUUID().toString();
        this.activityId = activityId;
        this.startTime = Calendar.getInstance();
        this.endTime = endTime;
        this.isActive = false;
        this.startValue = startValue;
        this.endValue = endValue;
        this.finalValue = finalValue;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

}
