package utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao_Impl;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class SchedulerService extends Service {

    public void onCreate()
    {
        super.onCreate();
        Log.d("service", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("service", "service started command");
        String operation = intent.getStringExtra("operation");
        String activity = intent.getStringExtra("activityID");
        String eventID = intent.getStringExtra("eventID");

        Toast.makeText(getApplicationContext(), "Event " + eventID + " has been " + operation, Toast.LENGTH_SHORT).show();

        ArrayList<Rule> rules = getRules(activity);

        switch (operation) {
            case "enable": {
                handleEnable(rules);
                setIsActive(eventID, true);
            }
            case "disable": {
                handleDisable(rules);
                setIsActive(eventID, false);
            }
            case "setAlarm": {
                callScheduleAlarm(eventID);
            }
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    private void callScheduleAlarm(String eventId) {
        ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(getApplicationContext()).scheduleEventDao();

        ScheduleEventDb event = scheduleEventDao.findScheduleEventById(eventId);

        SetAlarmManager.setSchedulingAlarm(getApplicationContext(), event);
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

        for (RuleDb rule : dbRules) {
            rules.add(getRuleInstance(rule));
        }

        return rules;
    }

    private Rule getRuleInstance(RuleDb rule) {
        switch (rule.getSetting()) {
            case DRIVING_MODE: return new DrivingModeRule(getApplicationContext(), rule.getSettingValue());
            case NIGHT_MODE: return new NightModeRule(getApplicationContext(), rule.getSettingValue());
            case VOLUME: return new RingerRule(getApplicationContext(), rule.getSettingValue());
            case STEP_COUNT: return new StepCounterRule();
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
