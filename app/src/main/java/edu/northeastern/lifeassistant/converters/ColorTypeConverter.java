package edu.northeastern.lifeassistant.converters;

import androidx.room.TypeConverter;

import edu.northeastern.lifeassistant.types.ColorType;

public class ColorTypeConverter {

    @TypeConverter
    public static ColorType fromString(String value) {
        if(value.equals(ColorType.RED.getString())) {
            return ColorType.RED;
        } else if(value.equals(ColorType.BLUE.getString())) {
            return ColorType.BLUE;
        } else if(value.equals(ColorType.GREEN.getString())) {
            return ColorType.GREEN;
        } else if(value.equals(ColorType.YELLOW.getString())) {
            return ColorType.YELLOW;
        } else if(value.equals(ColorType.ORANGE.getString())) {
            return ColorType.ORANGE;
        } else if(value.equals(ColorType.PURPLE.getString())) {
            return ColorType.PURPLE;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromColorType(ColorType value) {
        return value == null ? null : value.getString();
    }

}
