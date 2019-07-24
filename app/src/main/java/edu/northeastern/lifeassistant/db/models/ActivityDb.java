package edu.northeastern.lifeassistant.db.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.UUID;

import edu.northeastern.lifeassistant.db.types.ColorType;

@Entity(tableName = "activities")
public class ActivityDb {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    private ColorType color;

    public ActivityDb(String name, ColorType color) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

}
