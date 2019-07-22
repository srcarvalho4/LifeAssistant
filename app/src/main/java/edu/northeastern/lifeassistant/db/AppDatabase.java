package edu.northeastern.lifeassistant.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import edu.northeastern.lifeassistant.converters.ColorTypeConverter;
import edu.northeastern.lifeassistant.converters.DateConverter;
import edu.northeastern.lifeassistant.converters.DaysOfWeekConverter;
import edu.northeastern.lifeassistant.converters.SettingTypeConverter;
import edu.northeastern.lifeassistant.dao.ActivityDao;
import edu.northeastern.lifeassistant.dao.RuleDao;
import edu.northeastern.lifeassistant.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.dao.SettingDao;
import edu.northeastern.lifeassistant.models.Activity;
import edu.northeastern.lifeassistant.models.Rule;
import edu.northeastern.lifeassistant.models.ScheduleEvent;
import edu.northeastern.lifeassistant.models.Setting;

@Database(entities = {
                Activity.class,
                ScheduleEvent.class,
                Rule.class,
                Setting.class
            }, version = 1)
@TypeConverters({
        ColorTypeConverter.class,
        DateConverter.class,
        DaysOfWeekConverter.class,
        SettingTypeConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ActivityDao activityDao();
    public abstract ScheduleEventDao scheduleEventDao();
    public abstract RuleDao ruleDao();
    public abstract SettingDao settingDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "user-database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
