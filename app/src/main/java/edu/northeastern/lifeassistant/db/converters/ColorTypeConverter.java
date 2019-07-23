package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;
import edu.northeastern.lifeassistant.db.types.ColorType;

public class ColorTypeConverter {

    @TypeConverter
    public static ColorType fromString(String colorString) {
        if(colorString.equals(ColorType.RED.getString())) {
            return ColorType.RED;
        } else if(colorString.equals(ColorType.BLUE.getString())) {
            return ColorType.BLUE;
        } else if(colorString.equals(ColorType.GREEN.getString())) {
            return ColorType.GREEN;
        } else if(colorString.equals(ColorType.YELLOW.getString())) {
            return ColorType.YELLOW;
        } else if(colorString.equals(ColorType.ORANGE.getString())) {
            return ColorType.ORANGE;
        } else if(colorString.equals(ColorType.PURPLE.getString())) {
            return ColorType.PURPLE;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromColorType(ColorType colorType) {
        if(colorType == null) {
            return null;
        }

        return colorType.getString();
    }

}
