package edu.northeastern.lifeassistant.utils.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.activities.activity.ActivityScreen;
import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.event.ScheduleScreen;
import edu.northeastern.lifeassistant.activities.spontaneous.SpontaneousActive;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.utils.rules.DrivingModeRule;
import edu.northeastern.lifeassistant.utils.rules.NightModeRule;
import edu.northeastern.lifeassistant.utils.rules.RingerRule;
import edu.northeastern.lifeassistant.utils.rules.Rule;
import edu.northeastern.lifeassistant.utils.rules.StepCounterRule;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class SchedulerService extends Service {

    private String CHANNEL_ID = "";

    public void onCreate()
    {
        super.onCreate();
        Log.d("service", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("service", "service started command");
        String operation = intent.getStringExtra("operation");
        String activity = intent.getStringExtra("activity");
        String eventID = intent.getStringExtra("eventID");
        String eventName = intent.getStringExtra("eventName");
        int alarmID = intent.getIntExtra("alarmID", 0);

        Log.d("service", "activityID " + activity);
        Log.d("service", "operation " + operation);
        Log.d("service", "eventID: " + eventID);

        ArrayList<Rule> rules = getRules(activity);

        if (operation != null) {
            switch (operation) {
                case "enable": {
                    Log.d("setAlarm", "enabling rules!");
                    //enableNotification(eventID);
                    makeNotification(eventID, "enable", "Event started",
                            "Your scheduled event " + eventName + " has been started and its rules are currently enabled.",
                            "Disable Event Now");
                    handleEnable(rules);
                    removeAlarmID(alarmID, eventID);
                    setIsActive(eventID, true);
                    break;
                }
                case "disable": {
                    Log.d("setAlarm", "disabling rules!");
                    makeNotification(eventID, "disable", "Event Ended",
                            "Your scheduled event " + eventName + " has ended, and its rules are now disabled.",
                            null);
                    handleDisable(rules);
                    removeAlarmID(alarmID, eventID);
                    setIsActive(eventID, false);
                    break;
                }
                case "setAlarm": {
                    Log.d("setAlarm", "calling callScheduleAlarm from service");
                    removeAlarmID(alarmID, eventID);
                    callScheduleAlarm(eventID);
                    break;
                }
                case "reminder": {
                    removeAlarmID(alarmID, eventID);
                    //reminderNotification(eventID);
                    makeNotification(eventID, "reminder", "Event starting soon",
                            "Your scheduled event " + eventName + " is starting in 10 minutes.",
                            "Cancel event");
                    Log.d("setAlarm", "reminder being sent!");
                    break;
                }
                case "cancelUpcoming": {
                    Log.d("service", "calling cancel upcoming on eventID: " + eventID);
                    cancelNotification(getNoficationID(eventID, "reminder"));
                    SetAlarmManager.cancelUpcomingNotStarted(getApplicationContext(), eventID);
                    break;
                }
                case "endEventEarly": {
                    cancelNotification(getNoficationID(eventID, "enable"));
                    SetAlarmManager.endEventEarly(getApplicationContext(), eventID);
                    break;
                }
            }
        } else {
            Log.e("setAlarm", "invalid operation!!");
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    // Creates a unique notification ID for the notification/operation.
    private int getNoficationID(String eventID, String operation) {
        return (eventID + operation).hashCode();
    }

    //Abstracted code for creating a notification
    private void makeNotification(String eventID, String operation, String notifTitle, String notifBody, String buttonText) {
        createNotificationChannel();

        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(getApplicationContext()).scheduleEventDao();
        ScheduleEventDb eventDb = scheduleEventDao.findScheduleEventById(eventID);

        Class<?> nextActivity;

        switch (operation) {
            case "enable": {
                nextActivity = SpontaneousActive.class;
                break;
            }
            case "reminder": {
                nextActivity = ScheduleScreen.class;
                break;
            }
            case "disable": {
                nextActivity = ScheduleScreen.class;
                break;
            }
            default: {
                nextActivity = ActivityScreen.class;
                break;
            }

        }

        Intent intent = new Intent(this, nextActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.lightning_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                //.addAction(R.drawable.delete_icon, buttonText, cancelEventPendingIntent)
                .setAutoCancel(true);

        if (buttonText != null) {
            Intent buttonIntent = new Intent(this, ActionReceiverAlarm.class);
            //buttonIntent.setAction(ACTION_SNOOZE);
            buttonIntent.putExtra("operation", operation);
            buttonIntent.putExtra("eventID", eventDb.getId());
            buttonIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            PendingIntent buttonPendingEvent = PendingIntent.getBroadcast(
                    this, buttonIntent.hashCode(), buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.addAction(R.drawable.delete_icon, buttonText, buttonPendingEvent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        int notificationID = getNoficationID(eventID, operation);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationID, builder.build());
    }

    //Clear the notification from the notifications bar - to be called when action button is hit.
    private void cancelNotification(int notificationID) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.cancel(notificationID);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for sending event reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void removeAlarmID(int alarmID, String eventId) {
        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(getApplicationContext()).scheduleEventDao();

        ScheduleEventDb event = scheduleEventDao.findScheduleEventById(eventId);

        List<Integer> alarmIDs = event.getAlarmIds();

        if (alarmIDs != null && alarmIDs.contains(alarmID)) {
            int index = alarmIDs.indexOf(alarmID);
            alarmIDs.remove(index);
        }
    }

    private void callScheduleAlarm(String eventId) {
        SetAlarmManager.setSchedulingAlarm(getApplicationContext(), eventId);
    }

    private void setIsActive(String eventID, boolean enabled) {
        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(getApplicationContext()).scheduleEventDao();

        ScheduleEventDb scheduleEventDb = scheduleEventDao.findScheduleEventById(eventID);

        //set is active
        scheduleEventDb.setActive(enabled);
        scheduleEventDao.update(scheduleEventDb);
    }

    private ArrayList<Rule> getRules(String activityID) {
        //1) get ActivityDB from data base
        //2) get list of rule IDs
        //3) for each list of rules, switch on setting type + create appropriate one with value
        //4) add to list of rules and return

        ArrayList<Rule> rules = new ArrayList<>();

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

        List<RuleDb> dbRules = db.ruleDao().findRulesForActivity(activityID);
        Log.d("gettingRules", "activity ID: " + activityID + " rule size" + dbRules.size());

        for (RuleDb rule : dbRules) {
            Log.d("gettingRules", "got a rule!! " + rule.getSetting());
            rules.add(getRuleInstance(rule));
        }

        return rules;
    }

    private Rule getRuleInstance(RuleDb rule) {
        switch (rule.getSetting()) {
            case DRIVING_MODE: return new DrivingModeRule(getApplicationContext(), rule.getSettingValue());
            case NIGHT_MODE: return new NightModeRule(getApplicationContext(), rule.getSettingValue());
            case RINGER: return new RingerRule(getApplicationContext(), rule.getSettingValue());
            case STEP_COUNT: return new StepCounterRule(getApplicationContext());

            default: throw new IllegalArgumentException("need a valid state type");
        }
    }

    private void handleEnable(ArrayList<Rule> rules) {
        for (Rule r: rules) {
            r.enable();
            Log.d("setAlarm", "enabled rule "+ r.toString());
        }
    }

    private void handleDisable(ArrayList<Rule> rules) {
        for (Rule r: rules) {
            r.disable();
            Log.d("setAlarm", "disabled rule "+ r.toString());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
