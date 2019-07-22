package edu.northeastern.lifeassistant.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "rules",
        foreignKeys = {
            @ForeignKey(entity = Activity.class,
                parentColumns = "id",
                childColumns = "activity_id",
                onUpdate = CASCADE,
                onDelete = CASCADE),
            @ForeignKey(entity = Activity.class,
                parentColumns = "id",
                childColumns = "setting_id",
                onUpdate = CASCADE,
                onDelete = CASCADE)
        })
public class Rule {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "activity_id")
    private Long activityId;

    @ColumnInfo(name = "setting_id")
    private Long settingId;

    @ColumnInfo(name = "setting_state")
    private Boolean settingState;

    @ColumnInfo(name = "setting_value")
    private String settingValue;

    public Rule(Long activityId, Long settingId, Boolean settingState, String settingValue) {
        this.activityId = activityId;
        this.settingId = settingId;
        this.settingState = settingState;
        this.settingValue = settingValue;
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

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public Boolean getSettingState() {
        return settingState;
    }

    public void setSettingState(Boolean settingState) {
        this.settingState = settingState;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

}
