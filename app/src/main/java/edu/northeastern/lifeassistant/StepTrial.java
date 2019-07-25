package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.Activity;
import edu.northeastern.lifeassistant.db.models.SpontaneousEvent;
import edu.northeastern.lifeassistant.db.types.ColorType;

public class StepTrial extends AppCompatActivity {

    Button buttonStart;
    Button buttonStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_trial);

        buttonStart = findViewById(R.id.startButtonRunning);
        buttonStop = findViewById(R.id.stopButtonRunning);



        //1. connect to the DB
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        Activity activity = new Activity("Running", ColorType.BLUE);
        db.activityDao().insert(activity);

        //2. Check if most recent activity has a end time
        SpontaneousEvent mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
        if (mostRecentEvent != null)
        {
            if (mostRecentEvent.getEndTime() != null) {
                //enable the start button
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
            }
        }


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpontaneousEvent sEvent = new SpontaneousEvent(activity.getId(), null, "200", null, null);
                db.spontaneousEventDao().insert(sEvent);
                buttonStop.setEnabled(true);
                buttonStart.setEnabled(false);
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpontaneousEvent mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
                mostRecentEvent.setEndTime(Calendar.getInstance());
                mostRecentEvent.setEndValue("800");
                db.spontaneousEventDao().update(mostRecentEvent);
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                printData();
            }
        });




        /*
        SpontaneousEvent mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
        if (mostRecentEvent.getEndTime() == null) {
            buttonStart.setEnabled(false);
        }
        */

        //db.spontaneousEventDao().findMostRecentEvent().setEndTime(Calendar.getInstance());
        //db.spontaneousEventDao().findMostRecentEvent().setActive(false);





        //2. check database for most recent entry

        //i. max(startTime) column
        //ii. check if endTime for corresponding entry is greater than startTime
            //a. If not, the event is still running
        //buttonStart.setEnabled(false);

            //b. If yes, the event has ended

        //
        //Calendar calendar = Calendar.getInstance();
        //long timeInMilliSeconds = calendar.getTimeInMillis();

        //write logic for startButton
            //startButton();
            //fetch current time

        //write logic for endButton
        //endButton();



    }

    public void printData() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        SpontaneousEvent check = db.spontaneousEventDao().findMostRecentEvent();
        Calendar endtime = check.getEndTime();
        String endValue = check.getEndValue();
        String startValue = check.getStartValue();
        Calendar startTime = check.getStartTime();



        Log.d("Steps", startTime.getTime().toString());
        Log.d("Steps", startValue.toString());
        Log.d("Steps", endtime.getTime().toString());
        Log.d("Steps", endValue.toString());
    }

}
