package utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

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
        Log.d("setAlarm", "handling intent in service");

        Activity activity = new Activity(Color.rgb(255,0,0), "Running", new ArrayList<Rule>());

        Toast.makeText(getApplicationContext(), "Activity " + activity.typeName + " has been " + operation, Toast.LENGTH_SHORT).show();

        // Put here YOUR code.
        ArrayList<Rule> rules = activity.rules;
        for (Rule r: rules) {
            if (operation != null && operation.equals("enable")) {
                r.enable();
                Log.d("setAlarm", "enabled rule "+ r.toString());
            } else {
                r.disable();
                Log.d("setAlarm", "disabled rule "+ r.toString());
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
