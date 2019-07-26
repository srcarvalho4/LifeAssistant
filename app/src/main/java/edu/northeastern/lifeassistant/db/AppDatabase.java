package edu.northeastern.lifeassistant.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

import edu.northeastern.lifeassistant.db.converters.ColorTypeConverter;
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
import edu.northeastern.lifeassistant.db.types.ColorType;

@Database(entities = {
                ActivityDb.class,
                ScheduleEventDb.class,
                RuleDb.class,
                SpontaneousEventDb.class
            }, version = 1)
@TypeConverters({
        ColorTypeConverter.class,
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
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                "life-assistant")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread() {
                            @Override
                            public void run() {
                                prepopulateDb(getAppDatabase(context));
                            }
                        }.start();
                    }
                })
                .allowMainThreadQueries()
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static void prepopulateDb(AppDatabase db) {
//        db.activityDao().insert(new ActivityDb("Running", ColorType.BLUE));
    }

}
