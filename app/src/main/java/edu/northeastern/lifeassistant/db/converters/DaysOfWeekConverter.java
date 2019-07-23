package edu.northeastern.lifeassistant.db.converters;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DaysOfWeekConverter {

    @TypeConverter
    public static String fromIntegerList(List<Integer> integerList) {
        if(integerList == null || integerList.isEmpty()) {
            return null;
        }

        return integerList.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "");
    }

    @TypeConverter
    public static List<Integer> fromString(String csv) {
        if(csv == null) {
            return null;
        }

        return Arrays.stream(csv.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
