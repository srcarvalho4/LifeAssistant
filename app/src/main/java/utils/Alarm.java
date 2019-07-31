package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("setAlarm", "Received intent!");
        String operation = intent.getStringExtra("operation");
        String activity = intent.getStringExtra("activity");
        String eventID = intent.getStringExtra("eventID");
        Toast.makeText(context, "Received intent! " + operation, Toast.LENGTH_SHORT).show();

        Intent passalongIntent = new Intent(context, SchedulerService.class);
        passalongIntent.putExtra("operation", operation);
        passalongIntent.putExtra("activity", activity);
        passalongIntent.putExtra("eventID", eventID);
        context.startService(passalongIntent);

    }

}
