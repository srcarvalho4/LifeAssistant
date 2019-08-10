package edu.northeastern.lifeassistant.utils.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActionReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("setAlarm", "RECEIVED ACTION!!!");

        String operation = intent.getStringExtra("operation");
        String eventID = intent.getStringExtra("eventID");

        String nextOp = "";

        switch (operation) {
            case "reminder": {
                nextOp = "cancelUpcoming";
                break;
            }
            case "enable": {
                nextOp = "endEventEarly";
                break;
            }
        }

        Intent passalong = new Intent(context, SchedulerService.class);
        passalong.putExtra("operation", nextOp);
        passalong.putExtra("eventID", eventID);
        context.startService(passalong);

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
