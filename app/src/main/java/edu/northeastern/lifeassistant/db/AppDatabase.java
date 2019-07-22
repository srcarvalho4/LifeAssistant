package edu.northeastern.lifeassistant.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import edu.northeastern.lifeassistant.db.converters.ColorTypeConverter;
import edu.northeastern.lifeassistant.db.converters.DateConverter;
import edu.northeastern.lifeassistant.db.converters.DaysOfWeekConverter;
import edu.northeastern.lifeassistant.db.converters.SettingTypeConverter;
import edu.northeastern.lifeassistant.db.dao.ActivityDao;
import edu.northeastern.lifeassistant.db.dao.RuleDao;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.dao.SettingDao;
import edu.northeastern.lifeassistant.db.models.Activity;
import edu.northeastern.lifeassistant.db.models.Rule;
import edu.northeastern.lifeassistant.db.models.ScheduleEvent;
import edu.northeastern.lifeassistant.db.models.Setting;

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
