package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.dao.SpontaneousEventDao;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;

public class SetAlarmManager {

    //Sets the alarm that will schedule all other alarms. Will schedule one week of alarms, and then
    // set an alarm for 1 week away to do the same
    public static void setSchedulingAlarm(Context context, String eventID) {
        Log.d("setAlarm", "in setSchedulingAlarm");
        Calendar time = Calendar.getInstance();
        time.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        time.setTimeInMillis(System.currentTimeMillis());
        Log.d("setAlarm", "current time is " + time.getTime().toString());
        int firstDay = time.get(Calendar.DAY_OF_WEEK);

        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(context).scheduleEventDao();
        ScheduleEventDb event = scheduleEventDao.findScheduleEventById(eventID);
        List<Integer> days = event.getDaysOfWeek();

        for (Integer day : days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            calendar.setTimeInMillis(System.currentTimeMillis());

            Calendar eventStartTime = Calendar.getInstance();
            eventStartTime.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            eventStartTime.setTimeInMillis(System.currentTimeMillis());
            eventStartTime.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
            eventStartTime.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));

            if (day < firstDay) {
                //day already happened this week, set it for next week (+1week - days passed already)
                calendar.add(Calendar.DAY_OF_YEAR, 7 - (firstDay - day));
            } else if (day > firstDay) {
                //day hasn't happened yet this wee, add number of days between firstDay and this day
                calendar.add(Calendar.DAY_OF_YEAR, (day - firstDay));
            } else {
                //today is that day! if hour time already happened, set next week. otherwise, set today
                Log.d("setAlarm", "same day! start time " + event.getStartTime().getTime().toString());
                if (eventStartTime.after(time)) {
                    //Start time has not happened yet today, so set for today
                    calendar.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
                    calendar.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));
                    Log.d("actiityID", event.getActivityId());
                    setStartAlarm(context, event.getActivityId(), event, calendar);
                } else {
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    calendar.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
                    calendar.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));
                    Log.d("actiityID", event.getActivityId());
                    setStartAlarm(context, event.getActivityId(), event, calendar);
                }

                if (event.getReminderSwitchState()) {
                    Calendar reminder = Calendar.getInstance();
                    reminder.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                    reminder.setTimeInMillis(calendar.getTimeInMillis());
                    reminder.add(Calendar.MINUTE, -1 * 10);
                    setReminder(context, event, reminder);
                }

                Calendar endTime = Calendar.getInstance();
                endTime.setTimeInMillis(calendar.getTimeInMillis());
                endTime.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                endTime.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
                endTime.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));
                Log.d("actiityID", event.getActivityId());
                setEndAlarm(context, event.getActivityId(), event, endTime);

                scheduleEventDao.update(event);


            }
        }

        //1) set alarm for each day in between firstDay and firstDay+1week
        //2) set alarm for firstDay+1week

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        time.add(Calendar.DAY_OF_YEAR, 7);
        Log.d("setAlarm", "next setSchedule set for " + time.getTime().toString());
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

    private static void setReminder(Context context, ScheduleEventDb eventDb, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        int alarmID = makeHashRequestCode(eventDb, time);

        List<Integer> alarmIDs = eventDb.getAlarmIds();
        if (alarmIDs == null) {
            alarmIDs = new ArrayList<Integer>();
        }
        alarmIDs.add(alarmID);
        eventDb.setAlarmIds(alarmIDs);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "reminder");
        i1.putExtra("eventID", eventDb.getId());
        i1.putExtra("alarmID", alarmID);

        PendingIntent pi1 = PendingIntent.getBroadcast(context, alarmID, i1, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi1);
    }

    private static void setStartAlarm(Context context, String activityID, ScheduleEventDb eventDb, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        int alarmID = makeHashRequestCode(eventDb, time);

        List<Integer> alarmIDs = eventDb.getAlarmIds();
        if (alarmIDs == null) {
            alarmIDs = new ArrayList<Integer>();
        }
        alarmIDs.add(alarmID);
        eventDb.setAlarmIds(alarmIDs);

        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "enable");
        i1.putExtra("activity", activityID);
        i1.putExtra("eventID", eventDb.getId());
        i1.putExtra("eventName", eventDb.getName());
        i1.putExtra("alarmID", alarmID);

        PendingIntent pi1 = PendingIntent.getBroadcast(context, alarmID, i1, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi1);
    }

    private static void setEndAlarm(Context context, String activityID, ScheduleEventDb eventDb, Calendar time) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int alarmID = makeHashRequestCode(eventDb, time);

        List<Integer> alarmIDs = eventDb.getAlarmIds();
        if (alarmIDs == null) {
            alarmIDs = new ArrayList<Integer>();
        }
        alarmIDs.add(alarmID);

        eventDb.setAlarmIds(alarmIDs);

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        i2.putExtra("operation", "disable");
        i2.putExtra("activity", activityID);
        i2.putExtra("eventID", eventDb.getId());
        i2.putExtra("eventName", eventDb.getName());
        i2.putExtra("alarmID", alarmID);

        PendingIntent pi2 = PendingIntent.getBroadcast(context, alarmID, i2, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, time.getTimeInMillis(), pi2);
    }


    //Called when an event is deleted / edited - cancels all pending alarms.
    public static void cancelAllPending(Context context, List<Integer> alarms) {

        if (alarms != null) {
            for (int alarm : alarms) {
                Intent i = new Intent(context, Alarm.class);
                PendingIntent pi = PendingIntent.getBroadcast(context, alarm, i, 0);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                am.cancel(pi);
            }
        }

    }


    //To be called when someone wants to end their currently running event before it is supposed to end.
    public static void endEventEarly(Context context, ScheduleEventDb event) {
        // 1) cancel ending alarm (only for today!)
        // 2) remove that alarm ID from the event list of alarmIDs.
        // 3) DISABLE ALL THE RULES!!

        List<Integer> alarmIDs = event.getAlarmIds();
        if (alarmIDs == null) {
            alarmIDs = new ArrayList<>();
        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        //Cancel the pending alarm for today
        Calendar time = Calendar.getInstance();
        time.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        time.setTimeInMillis(System.currentTimeMillis());

        time.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
        time.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));

        int alarmID = makeHashRequestCode(event, time);
        int index = alarmIDs.indexOf(alarmID);

        if (index != -1) {
            Intent i = new Intent(context, Alarm.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, alarmID, i, 0);
            am.cancel(pi);
        }

        //Remove the alarm ID from the list of alarm IDs
        alarmIDs.remove(index);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        calendar.setTimeInMillis(System.currentTimeMillis());

        int newAlarmID = makeHashRequestCode(event, calendar);

        alarmIDs.add(newAlarmID);
        event.setAlarmIds(alarmIDs);

        Intent i = new Intent(context, Alarm.class);
        i.putExtra("operation", "disable");
        i.putExtra("activity", event.getActivityId());
        i.putExtra("eventID", event.getId());
        i.putExtra("alarmID", newAlarmID);
        PendingIntent pi = PendingIntent.getBroadcast(context, newAlarmID, i, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(), pi);

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
