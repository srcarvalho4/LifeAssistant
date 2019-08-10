package edu.northeastern.lifeassistant.activities.spontaneous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import edu.northeastern.lifeassistant.activities.history.HistoryActivity;
import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;

public class SpontaneousStepCounterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    String myTotalSteps = "lol";
    //to store startValue
    String myStartStepsString = "";
    int myStartSteps = 0;

    //to store currentValue
    String myCurrentStepsString = "";
    int myCurrentSteps = 0;


    //to store Difference
    String myFinalStepsString = "";
    int myFinalSteps = 0;



    Button buttonStart;
    Button buttonStop;
    ProgressBar progressBar;
    Button viewHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_step_counter);
        //initViews();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        buttonStart = findViewById(R.id.startButtonRunning1);
        buttonStop = findViewById(R.id.stopButtonRunning1);
        progressBar = findViewById(R.id.pBar);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);


        //new ViewTodaysStepCountTask().execute();

        //1. connect to the DB
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        ActivityDb activity = db.activityDao().findActivityByName("Running");
        //ActivityDb activity = new ActivityDb("Running", Color.BLUE);
        //db.activityDao().insert(activity);
        //SpontaneousEvent sEvent = new SpontaneousEvent(activity.getId(), null, "200", null, "200");
        //db.spontaneousEventDao().insert(sEvent);

        //2. Check if most recent activity has a end time
        SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
        if (mostRecentEvent != null)
        {
            if (mostRecentEvent.getEndTime() != null) {
                //enable the start button and disable stop button
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
            }
        }


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                new ViewTodaysStepCountTask().execute();
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    Thread.sleep(2500);
                }
                catch (Exception e) {

                }
                progressBar.setVisibility(View.INVISIBLE);
                SpontaneousEventDb sEvent = new SpontaneousEventDb(activity.getId(), null, myTotalSteps, null, null);
                db.spontaneousEventDao().insert(sEvent);
                buttonStop.setEnabled(true);
                buttonStart.setEnabled(false);
                SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
                mostRecentEvent.setEndTime(Calendar.getInstance());
                mostRecentEvent.setEndValue(null);
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                new ViewTodaysStepCountTask().execute();
                try {
                    Thread.sleep(2500);
                }
                catch (Exception e) {

                }
                progressBar.setVisibility(View.INVISIBLE);
                SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
                myStartStepsString = mostRecentEvent.getStartValue();
                //Log.d("MYTAG", myStartStepsString);
                myStartSteps = Integer.parseInt(myStartStepsString);

                myCurrentSteps = Integer.parseInt(myTotalSteps);
                //Log.d("MYTAG", Integer.toString(myStartSteps));

                myFinalSteps = myCurrentSteps - myStartSteps;
                myFinalStepsString = Integer.toString(myFinalSteps);

                mostRecentEvent.setEndTime(Calendar.getInstance());
                mostRecentEvent.setEndValue(myTotalSteps);
                mostRecentEvent.setFinalValue(myFinalStepsString);
                //mostRecentEvent.setActive(true);

                db.spontaneousEventDao().update(mostRecentEvent);
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                printData();
            }
        });

        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpontaneousStepCounterActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    public void printData() {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        SpontaneousEventDb check = db.spontaneousEventDao().findMostRecentEvent();
        Calendar endtime = check.getEndTime();
        String endValue = check.getEndValue();
        String startValue = check.getStartValue();
        Calendar startTime = check.getStartTime();
        String finalValue = check.getFinalValue();


        String printString = "Start Time: " + startTime.getTime().toString() + "\nEnd Time: " + endtime.getTime().toString() +
                "\nStep Count" + finalValue;

        Toast.makeText(getApplicationContext(), printString, Toast.LENGTH_LONG).show();



        Log.d("Steps", startTime.getTime().toString());
        Log.d("Steps", startValue);
        Log.d("Steps", endtime.getTime().toString());
        Log.d("Steps", endValue);
        Log.d("Steps", check.getActivityId().toString());
        Log.d("Steps", finalValue);

    }


    private void showDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.e("History", "Data point:");
            Log.e("History", "\tType: " + dp.getDataType().getName());
            Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                myTotalSteps = dp.getValue(field).toString();
            }
        }
        //Log.d("Steps", myTotalSteps);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("HistoryAPI", "onConnected");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");

    }

    private void displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal( mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA ).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }


    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }
    }
}
