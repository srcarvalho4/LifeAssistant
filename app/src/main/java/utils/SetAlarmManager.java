package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.dao.SpontaneousEventDao;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;

public class SetAlarmManager {

    //Sets the alarm that will schedule all other alarms. Will schedule one week of alarms, and then
    // set an alarm for 1 week away to do the same
    public static void setSchedulingAlarm(Context context, ScheduleEventDb event) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        int firstDay = time.get(Calendar.DAY_OF_WEEK);

        List<Integer> days = event.getDaysOfWeek();

        for (Integer day : days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            if (day < firstDay) {
                //day already happened this week, set it for next week (+1week - days passed already)
                calendar.add(Calendar.DAY_OF_YEAR, 7 - (firstDay - day));
            } else if (day > firstDay) {
                //day hasn't happened yet this wee, add number of days between firstDay and this day
                calendar.add(Calendar.DAY_OF_YEAR, (day - firstDay));
            } else {
                //today is that day! if hour time already happened, set next week. otherwise, set today
                if (event.getStartTime().get(Calendar.HOUR) > calendar.get(Calendar.HOUR)
                || (event.getStartTime().get(Calendar.HOUR) == calendar.get(Calendar.HOUR) &&
                        event.getStartTime().get(Calendar.MINUTE) > calendar.get(Calendar.MINUTE))) {
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                }
                calendar.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
                calendar.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));
                setStartAlarm(context, event.getActivityId(), event, calendar);

                if (event.getReminderSwitchState()) {
                    calendar.add(Calendar.MINUTE, -1 * 10);
                    setReminder(context, event, calendar);
                }

                calendar.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
                calendar.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));
                setEndAlarm(context, event.getActivityId(), event, calendar);


            }
        }

        //1) set alarm for each day in between firstDay and firstDay+1week
        //2) set alarm for firstDay+1week

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        time.add(Calendar.DAY_OF_YEAR, 7);
        Intent intent = new Intent(context, Alarm.class);
        intent.putExtra("operation", "setAlarms");
        PendingIntent pi = PendingIntent.getBroadcast(context, event.hashCode(), intent, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi);
    }


    //Returns the activity ID of the currently active event, else returns null
    public static String getActiveScheduleEvent(Context context) {
        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(context).scheduleEventDao();
        SpontaneousEventDao spontaneousEventDao = AppDatabase.getAppDatabase(context).spontaneousEventDao();

        List<ScheduleEventDb> scheduleEvents = scheduleEventDao.findAllScheduleEvents();

        for (ScheduleEventDb s : scheduleEvents) {
            if (s.getActive()) {
                return s.getActivityId();
            }
        }

        List<SpontaneousEventDb> spontaneousEvents = spontaneousEventDao.findAllSpontaneousEvents();

        for (SpontaneousEventDb s : spontaneousEvents) {
            if (s.getActive()) {
                return s.getActivityId();
            }
        }

        return null;
    }

    private static void setReminder(Context context, ScheduleEventDb event, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "reminder");
        i1.putExtra("eventID", event.getId());

        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, time), i1, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi1);
        Log.d("setAlarm", "set enable alarm in " + time.getTime().toString());
    }

    private static void setStartAlarm(Context context, String activityID, ScheduleEventDb eventDb, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "enable");
        i1.putExtra("activity", activityID);
        i1.putExtra("eventID", eventDb.getId());

        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(eventDb, time), i1, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi1);
        Log.d("setAlarm", "set enable alarm in " + time.getTime().toString());
    }

    private static void setEndAlarm(Context context, String activityID, ScheduleEventDb eventDb, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        i2.putExtra("operation", "disable");
        i2.putExtra("activity", activityID);
        i2.putExtra("eventID", eventDb.getId());

        PendingIntent pi2 = PendingIntent.getBroadcast(context, makeHashRequestCode(eventDb, time), i2, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi2);
        Log.d("setAlarm", "set disable alarm in 3 second " + time.getTime().toString());
    }



    //Statically cancel an alarm using the hashcode?? pending intent?
    public static void cancelEventAlarm(Context context, ScheduleEventDb event) {
        for (int day : event.getDaysOfWeek()) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, day);
            start.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
            start.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));

            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_WEEK, day);
            end.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
            end.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));

            cancelAlarm(context, event, start, end);
        }
    }

    private static void cancelAlarm(Context context, ScheduleEventDb event, Calendar startTime, Calendar endTime) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, startTime), i1, 0);
        am.cancel(pi1);
        Log.d("cancelAlarm", "canceled start alarm for  " + event.getId());

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        PendingIntent pi2 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, endTime), i1, 0);
        am.cancel(pi2);
        Log.d("cancelAlarm", "canceled end alarm for  " + event.getId());
    }

    //Makes unique request code for the pending intent broadcast.
    private static int makeHashRequestCode(ScheduleEventDb event, Calendar time) {
        return event.hashCode() + time.hashCode();
    }
}
