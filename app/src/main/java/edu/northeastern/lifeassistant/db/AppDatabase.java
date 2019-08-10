package edu.northeastern.lifeassistant.db;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.converters.CalendarTimeConverter;
import edu.northeastern.lifeassistant.db.converters.DaysOfWeekConverter;
import edu.northeastern.lifeassistant.db.converters.SettingTypeConverter;
import edu.northeastern.lifeassistant.db.dao.ActivityDao;
import edu.northeastern.lifeassistant.db.dao.RuleDao;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
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
        db.activityDao().insert(new ActivityDb("Running", -4056997)); //activity_maroon
        db.activityDao().insert(new ActivityDb("Class", -7461718)); //activity_lavender
    }

}
