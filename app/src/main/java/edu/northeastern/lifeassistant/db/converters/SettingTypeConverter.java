package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;
import edu.northeastern.lifeassistant.db.types.SettingType;

public class SettingTypeConverter {

    @TypeConverter
    public static SettingType fromString(String settingString) {
        if(settingString.equals(SettingType.RINGER.getValue())) {
            return SettingType.RINGER;
        } else if(settingString.equals(SettingType.STEP_COUNT.getValue())) {
            return SettingType.STEP_COUNT;
        } else if(settingString.equals(SettingType.DRIVING_MODE.getValue())) {
            return SettingType.DRIVING_MODE;
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
