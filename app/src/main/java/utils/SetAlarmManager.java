package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class SetAlarmManager {

    //Sets a weekly repeating alarm for each day of the week at the set time for the given event.
    public static void setAlarm(Context context, ScheduleEvent event) {
        for (int day : event.days) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, day);
            start.set(Calendar.HOUR, event.startTime.get(Calendar.HOUR));
            start.set(Calendar.MINUTE, event.startTime.get(Calendar.MINUTE));

            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_WEEK, day);
            end.set(Calendar.HOUR, event.endTime.get(Calendar.HOUR));
            end.set(Calendar.MINUTE, event.endTime.get(Calendar.MINUTE));

            setAlarm(context, event.activityType, event, start, end);
        }
    }

    public static void deleteEventAlarms(Context context, ScheduleEvent event) {
        int eventHash = event.name.hashCode();

    }

    //Statically set up start / end time for alarm for 1 day/time
    //Must call this method for each day of week
    private static void setAlarm(Context context, Activity activity, ScheduleEvent event, Calendar startTime, Calendar endTime) {

        long weekInterval = 1000 * 60 * 60 * 24 * 7;

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        i1.putExtra("operation", "enable");
        i1.putExtra("activity", activity.getTypeName());
        i1.putExtra("eventName", event.name);

        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, startTime), i1, 0);
        //am.setExact(AlarmManager.RTC, startTime.getTimeInMillis(), pi1);
        am.setRepeating(AlarmManager.RTC, startTime.getTimeInMillis(), weekInterval, pi1);
        Log.d("setAlarm", "set enable alarm in " + startTime.getTime().toString());

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        i2.putExtra("operation", "disable");
        i2.putExtra("activity", activity.getTypeName());
        i2.putExtra("eventName", event.name);

        PendingIntent pi2 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, endTime), i1, 0);
        //am.setExact(AlarmManager.RTC, endTime.getTimeInMillis(), pi2);
        am.setRepeating(AlarmManager.RTC, endTime.getTimeInMillis(), weekInterval, pi2);
        Log.d("setAlarm", "set disable alarm in 3 second " + endTime.getTime().toString());

    }

    //Statically cancel an alarm using the hashcode?? pending intent?
    public static void cancelEventAlarm(Context context, ScheduleEvent event) {
        for (int day : event.days) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, day);
            start.set(Calendar.HOUR, event.startTime.get(Calendar.HOUR));
            start.set(Calendar.MINUTE, event.startTime.get(Calendar.MINUTE));

            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_WEEK, day);
            end.set(Calendar.HOUR, event.endTime.get(Calendar.HOUR));
            end.set(Calendar.MINUTE, event.endTime.get(Calendar.MINUTE));

            cancelAlarm(context, event, start, end);
        }
    }

    private static void cancelAlarm(Context context, ScheduleEvent event, Calendar startTime, Calendar endTime) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Start alarm
        Intent i1 = new Intent(context, Alarm.class);
        PendingIntent pi1 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, startTime), i1, 0);
        am.cancel(pi1);
        Log.d("cancelAlarm", "canceled start alarm for  " + event.name);

        //End Alarm
        Intent i2 = new Intent(context, Alarm.class);
        PendingIntent pi2 = PendingIntent.getBroadcast(context, makeHashRequestCode(event, endTime), i1, 0);
        am.cancel(pi2);
        Log.d("cancelAlarm", "canceled end alarm for  " + event.name);
    }

    //Makes unique request code for the pending intent broadcast.
    private static int makeHashRequestCode(ScheduleEvent event, Calendar time) {
        return event.hashCode() + time.hashCode();
    }
}
