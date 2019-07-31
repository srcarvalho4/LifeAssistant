package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class SetAlarmManager {

    //Sets a weekly repeating alarm for each day of the week at the set time for the given event.
    public static void setAlarm(Context context, ScheduleEventDb event) {
        for (int day : event.getDaysOfWeek()) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, day);
            start.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
            start.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));

            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_WEEK, day);
            end.set(Calendar.HOUR, event.getEndTime().get(Calendar.HOUR));
            end.set(Calendar.MINUTE, event.getEndTime().get(Calendar.MINUTE));

            setAlarm(context, event.getActivityId(), event, start, end);
        }
    }


    //Sets a reminder notification for the given event minutesBeforeStart minutes before the start time.
    //Note: does not set a notification for end of event
    public static void setReminder(Context context, ScheduleEventDb event, int minutesBeforeStart) {
        for (int day : event.getDaysOfWeek()) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, day);
            start.set(Calendar.HOUR, event.getStartTime().get(Calendar.HOUR));
            start.set(Calendar.MINUTE, event.getStartTime().get(Calendar.MINUTE));

            start.add(Calendar.MINUTE, -1 * minutesBeforeStart);

            setReminder(context, event, start);
        }
    }

    private static void setReminder(Context context, ScheduleEventDb event, Calendar time) {
        long weekInterval = 1000 * 60 * 60 * 24 * 7;

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "reminder");
        i1.putExtra("eventID", event.getId());

        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, time), i1, 0);
        am.setRepeating(AlarmManager.RTC, time.getTimeInMillis(), weekInterval, pi1);
        Log.d("setAlarm", "set enable alarm in " + time.getTime().toString());
    }

    //Statically set up start / end time for alarm for 1 day/time
    //Must call this method for each day of week
    private static void setAlarm(Context context, String activityID, ScheduleEventDb event, Calendar startTime, Calendar endTime) {

        long weekInterval = 1000 * 60 * 60 * 24 * 7;

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "enable");
        i1.putExtra("activity", activityID);
        i1.putExtra("eventID", event.getId());

        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, startTime), i1, 0);
        //am.setExact(AlarmManager.RTC, startTime.getTimeInMillis(), pi1);
        am.setRepeating(AlarmManager.RTC, startTime.getTimeInMillis(), weekInterval, pi1);
        Log.d("setAlarm", "set enable alarm in " + startTime.getTime().toString());

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        i2.putExtra("operation", "disable");
        i2.putExtra("activity", activityID);
        i2.putExtra("eventID", event.getId());

        PendingIntent pi2 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, endTime), i1, 0);
        am.setRepeating(AlarmManager.RTC, endTime.getTimeInMillis(), weekInterval, pi2);
        Log.d("setAlarm", "set disable alarm in 3 second " + endTime.getTime().toString());

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
