package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.UUID;
import edu.northeastern.lifeassistant.db.types.SettingType;

@Entity(tableName = "settings")
public class Setting {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private SettingType name;

    public Setting(SettingType name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SettingType getName() {
        return name;
    }

    public void setName(SettingType name) {
        this.name = name;
    }

}
