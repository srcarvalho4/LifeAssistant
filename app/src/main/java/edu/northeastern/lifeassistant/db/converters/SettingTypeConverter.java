package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;
import edu.northeastern.lifeassistant.db.types.SettingType;

public class SettingTypeConverter {

    @TypeConverter
    public static SettingType fromString(String settingString) {
        if(settingString.equals(SettingType.VOLUME.getValue())) {
            return SettingType.VOLUME;
        } else if(settingString.equals(SettingType.LOCATION.getValue())) {
            return SettingType.LOCATION;
        } else if(settingString.equals(SettingType.STEP_COUNT.getValue())) {
            return SettingType.STEP_COUNT;
        } else if(settingString.equals(SettingType.BRIGHTNESS.getValue())) {
            return SettingType.BRIGHTNESS;
        } else if(settingString.equals(SettingType.AIRPLANE_MODE.getValue())) {
            return SettingType.AIRPLANE_MODE;
        } else if(settingString.equals(SettingType.DO_NOT_DISTURB.getValue())) {
            return SettingType.DO_NOT_DISTURB;
        } else if(settingString.equals(SettingType.POWER_SAVER.getValue())) {
            return SettingType.POWER_SAVER;
        } else if(settingString.equals(SettingType.DRIVING_MODE.getValue())) {
            return SettingType.DRIVING_MODE;
        } else if(settingString.equals(SettingType.BLUETOOTH.getValue())) {
            return SettingType.BLUETOOTH;
        } else if(settingString.equals(SettingType.FLASHLIGHT.getValue())) {
            return SettingType.FLASHLIGHT;
        } else if (settingString.equals(SettingType.NIGHT_MODE.getValue())) {
            return SettingType.NIGHT_MODE;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromSettingType(SettingType settingType) {
        if(settingType == null) {
            return null;
        }
        return settingType.getValue();
    }

}
