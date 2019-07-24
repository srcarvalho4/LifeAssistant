package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.UUID;
import edu.northeastern.lifeassistant.db.types.SettingType;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "rules",
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
public class RuleDb {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "activity_id")
    private String activityId;

    @ColumnInfo(name = "setting")
    private SettingType setting;

    @ColumnInfo(name = "setting_value")
    private Integer settingValue;

    public RuleDb(String activityId, SettingType setting, Integer settingValue) {
        this.id = UUID.randomUUID().toString();
        this.activityId = activityId;
        this.setting = setting;
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

    public SettingType getSetting() {
        return setting;
    }

    public void setSetting(SettingType setting) {
        this.setting = setting;
    }

    public Integer getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(Integer settingValue) {
        this.settingValue = settingValue;
    }

}
