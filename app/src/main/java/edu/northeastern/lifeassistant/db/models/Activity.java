package edu.northeastern.lifeassistant.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.northeastern.lifeassistant.db.types.ColorType;

@Entity(tableName = "activities")
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color")
    private ColorType color;

    public Activity(String name, ColorType color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
