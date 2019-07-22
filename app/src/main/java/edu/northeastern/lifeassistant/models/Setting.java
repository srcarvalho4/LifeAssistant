package edu.northeastern.lifeassistant.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import edu.northeastern.lifeassistant.types.SettingType;

@Entity(tableName = "settings")
public class Setting {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private SettingType name;

    public Setting(SettingType name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SettingType getName() {
        return name;
    }

    public void setName(SettingType name) {
        this.name = name;
    }

}
