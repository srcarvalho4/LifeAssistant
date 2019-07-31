package edu.northeastern.lifeassistant.db;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import edu.northeastern.lifeassistant.db.converters.CalendarTimeConverter;
import edu.northeastern.lifeassistant.db.converters.DaysOfWeekConverter;
import edu.northeastern.lifeassistant.db.converters.SettingTypeConverter;
import edu.northeastern.lifeassistant.db.dao.ActivityDao;
import edu.northeastern.lifeassistant.db.dao.RuleDao;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
<<<<<<< HEAD
import edu.northeastern.lifeassistant.db.dao.SettingDao;
import edu.northeastern.lifeassistant.db.dao.SpontaneousEventDao;
import edu.northeastern.lifeassistant.db.models.Activity;
import edu.northeastern.lifeassistant.db.models.Rule;
import edu.northeastern.lifeassistant.db.models.ScheduleEvent;
import edu.northeastern.lifeassistant.db.models.Setting;
import edu.northeastern.lifeassistant.db.models.SpontaneousEvent;
import edu.northeastern.lifeassistant.db.types.ColorType;

@Database(entities = {
                Activity.class,
                ScheduleEvent.class,
                Rule.class,
                Setting.class,
        SpontaneousEvent.class
=======
import edu.northeastern.lifeassistant.db.dao.SpontaneousEventDao;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;

@Database(entities = {
                ActivityDb.class,
                ScheduleEventDb.class,
                RuleDb.class,
                SpontaneousEventDb.class
>>>>>>> 68cff8cc67a9decbf9955bcf5221dcba73fb6981
            }, version = 1)
@TypeConverters({
        CalendarTimeConverter.class,
        DaysOfWeekConverter.class,
        SettingTypeConverter.class
})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ActivityDao activityDao();

    public abstract ScheduleEventDao scheduleEventDao();

    public abstract RuleDao ruleDao();

    public abstract SpontaneousEventDao spontaneousEventDao();

    public abstract SpontaneousEventDao spontaneousEventDao();

    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = createAppDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase createAppDatabase(Context context) {
        RoomDatabase.Callback cb = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        prepopulateDb(getAppDatabase(context));
                    }
                });
            }
        };

        return  Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                "life-assistant")
                .addCallback(cb)
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static void prepopulateDb(AppDatabase db) {
        db.activityDao().insert(new ActivityDb("Running", Color.rgb(229, 115, 115)));
        db.activityDao().insert(new ActivityDb("Class", Color.rgb(149, 117, 205)));
    }

}
