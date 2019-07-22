package edu.northeastern.lifeassistant.converters;

import androidx.room.TypeConverter;
import edu.northeastern.lifeassistant.types.SettingType;

public class SettingTypeConverter {

    @TypeConverter
    public static SettingType fromString(String value) {
        if(value.equals(SettingType.VOLUME.getValue())) {
            return SettingType.VOLUME;
        } else if(value.equals(SettingType.LOCATION.getValue())) {
            return SettingType.LOCATION;
        } else if(value.equals(SettingType.STEP_COUNT.getValue())) {
            return SettingType.STEP_COUNT;
        } else if(value.equals(SettingType.BRIGHTNESS.getValue())) {
            return SettingType.BRIGHTNESS;
        } else if(value.equals(SettingType.AIRPLANE_MODE.getValue())) {
            return SettingType.AIRPLANE_MODE;
        } else if(value.equals(SettingType.DO_NOT_DISTURB.getValue())) {
            return SettingType.DO_NOT_DISTURB;
        } else if(value.equals(SettingType.POWER_SAVER.getValue())) {
            return SettingType.POWER_SAVER;
        } else if(value.equals(SettingType.DRIVING_MODE.getValue())) {
            return SettingType.DRIVING_MODE;
        } else if(value.equals(SettingType.BLUETOOTH.getValue())) {
            return SettingType.BLUETOOTH;
        } else if(value.equals(SettingType.FLASHLIGHT.getValue())) {
            return SettingType.FLASHLIGHT;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromSettingType(SettingType value) {
        return value == null ? null : value.getValue();
    }

}
