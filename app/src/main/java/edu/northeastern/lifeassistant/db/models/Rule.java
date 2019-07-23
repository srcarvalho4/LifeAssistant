package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.UUID;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "rules",
        foreignKeys = {
            @ForeignKey(entity = Activity.class,
                parentColumns = "id",
                childColumns = "activity_id",
                onUpdate = CASCADE,
                onDelete = CASCADE),
            @ForeignKey(entity = Setting.class,
                parentColumns = "id",
                childColumns = "setting_id",
                onUpdate = CASCADE,
                onDelete = CASCADE)
        },
        indices = {
            @Index(value="activity_id"),
            @Index(value="setting_id")
        })
public class Rule {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @ColumnInfo(name = "setting_id")
    private String settingId;

    @ColumnInfo(name = "setting_state")
    private Boolean settingState;

    @ColumnInfo(name = "setting_value")
    private String settingValue;

    public Rule(String activityId, String settingId, Boolean settingState, String settingValue) {
        this.id = UUID.randomUUID().toString();
        this.activityId = activityId;
        this.settingId = settingId;
        this.settingState = settingState;
        this.settingValue = settingValue;
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

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
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
