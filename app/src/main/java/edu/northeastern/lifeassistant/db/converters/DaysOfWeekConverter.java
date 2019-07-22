package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;

import java.util.HashSet;
import java.util.Set;
import edu.northeastern.lifeassistant.db.types.DayOfWeek;

public class DaysOfWeekConverter {

    @TypeConverter
    public static Integer fromDaysOfWeek(Set<DayOfWeek> value) {
        int b = 0;
        int position = 0;

        if(value == null) {
            return null;
        }

        for(DayOfWeek day : value) {
            switch (day) {
                case SUNDAY:
                    break;
                case MONDAY:
                    position = 1;
                    break;
                case TUESDAY:
                    position = 2;
                    break;
                case WEDNESDAY:
                    position = 3;
                    break;
                case THURSDAY:
                    position = 4;
                    break;
                case FRIDAY:
                    position = 5;
                    break;
                case SATURDAY:
                    position = 6;
                    break;
            }
            b = (b & ~(1 << position)) | (1 << position);
        }

        return b;
    }

    @TypeConverter
    public static Set<DayOfWeek> toDayOfWeek(Integer value) {
        Set<DayOfWeek> days = new HashSet<>();

        if(value == null) {
            return null;
        }
        if(isSetAtPosition(value, 0) == 1) {
            days.add(DayOfWeek.SUNDAY);
        }
        if(isSetAtPosition(value, 1) == 1) {
            days.add(DayOfWeek.MONDAY);
        }
        if(isSetAtPosition(value, 2) == 1) {
            days.add(DayOfWeek.TUESDAY);
        }
        if(isSetAtPosition(value, 3) == 1) {
            days.add(DayOfWeek.WEDNESDAY);
        }
        if(isSetAtPosition(value, 4) == 1) {
            days.add(DayOfWeek.THURSDAY);
        }
        if(isSetAtPosition(value, 5) == 1) {
            days.add(DayOfWeek.FRIDAY);
        }
        if(isSetAtPosition(value, 6) == 1) {
            days.add(DayOfWeek.SATURDAY);
        }

        return days;
    }

    private static int isSetAtPosition(int n, int pos) {
        return (n >> (pos - 1)) & 1;
    }

}
