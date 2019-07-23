package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;
import java.util.Calendar;

public class CalendarTimeConverter {

    @TypeConverter
    public static Calendar fromTimestamp(Long timestamp) {
        if(timestamp == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return calendar;
    }

    @TypeConverter
    public static Long fromDate(Calendar calendar) {
        if(calendar == null) {
            return null;
        }

        return calendar.getTimeInMillis();
    }

}
