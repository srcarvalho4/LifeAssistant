package edu.northeastern.lifeassistant.activities.spontaneous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import edu.northeastern.lifeassistant.activities.GoogleFitPopUp;
import edu.northeastern.lifeassistant.activities.history.HistoryActivity;
import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ScheduleEventDao;
import edu.northeastern.lifeassistant.db.dao.SpontaneousEventDao;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;
import edu.northeastern.lifeassistant.utils.items.Activity;
import edu.northeastern.lifeassistant.utils.adapters.RuleAdapter;
import edu.northeastern.lifeassistant.utils.items.RuleAdapterItem;
import edu.northeastern.lifeassistant.utils.services.SetAlarmManager;

public class SpontaneousActive extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    //===========================================
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
    Button viewHistoryButton;
    TextView spontaneousActiveStepCounter;
    //=============================================

    //ProgressBar progressBar;
    Timer timer;
    Boolean isRunning;
    String start = "Start";
    String stop = "Stop";
    int progress;
    TextView activityNameDisplay;
    String activityName;
    //Button button;

    ListView listView;
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_active);

        //==============****Google API Client *********========
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        //check if google fit is installed
        checkFitStatus();

        //=====================================================

        //==============Tie the button with layout============
        buttonStart = findViewById(R.id.SpontaneousActiveStartButton);
        buttonStop = findViewById(R.id.SpontaneousActiveStopButton);
        viewHistoryButton = findViewById(R.id.SpontaneousActiveViewHistoryButton);
        //=========================================================================

        //================ Layout specific ==========================
        Activity myActivity = new Activity(getApplicationContext(), getIntent().getStringExtra("name"));

        for (int i = 0; i < myActivity.getRules().size(); i++) {
            rules.add(new RuleAdapterItem(myActivity.getRules().get(i)));
        }

        activityName = myActivity.getName();
        int color = myActivity.getColor();

        String nameRender = activityName;

        activityNameDisplay = findViewById(R.id.SpontaneousActiveName);
        LinearLayout linearLayout = findViewById(R.id.SpontaneousActiveColorArea);

        activityNameDisplay.setText(nameRender);
        linearLayout.setBackgroundColor(color);

        listView = findViewById(R.id.SpontaneousActiveList);

        RuleAdapter adapter = new RuleAdapter(this, rules, false);

        listView.setAdapter(adapter);

        //=============================================================


        //============= 1.  Connecting with the Database==============================
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        //============================================================================

        //==========    2.  Check if most recent activity has a end time=================
        SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
        if (mostRecentEvent != null)
        {
            if (mostRecentEvent.getEndTime() != null) {
                //enable the start button and disable stop button
                activityNameDisplay.setText(nameRender + ": Inactive");
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
            }
            else {
                activityNameDisplay.setText(nameRender + ": Active");
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
            }
        }
        //=================================================================================

        if(!db.scheduleEventDao().findActiveEvents().isEmpty()) {
            activityNameDisplay.setText(nameRender + ": Active");
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(false);
        }

        //============  3.   Get the ActivityID =========================================
        ActivityDb activity = db.activityDao().findActivityByName(activityName);

        //============== 4. Button onClicklisteners ========================================


        //=========================  START Button code  ====================================
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enabling the stop button and disabling the stop button for reflecting the activity state
                activityNameDisplay.setText(nameRender + ": Active");
                buttonStop.setEnabled(true);
                buttonStart.setEnabled(false);
                //sets the myTotalSteps variable with current step count
                new ViewTodaysStepCountTask().execute();
                try {
                    //Added delay of 2.5 seconds to make an API call and fetch stepcount information
                    Thread.sleep(2500);
                }
                catch (Exception e) {

                }
                if (myTotalSteps.equals("lol"))
                {
                    buttonStop.setEnabled(false);
                    buttonStart.setEnabled(true);
                    Intent intent = new Intent(SpontaneousActive.this, GoogleFitPopUp.class);
                    intent.putExtra("FitInstalled", "yes");
                    startActivity(intent);
                }
                else {
                    //Adding/Inserting a new entry to the spontaneous activity database
                    SpontaneousEventDb sEvent = new SpontaneousEventDb(activity.getId(), null, myTotalSteps, null, null);
                    sEvent.setActive(true);
                    db.spontaneousEventDao().insert(sEvent);


                    //SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
                    //mostRecentEvent.setEndTime(Calendar.getInstance());
                    //mostRecentEvent.setEndValue(null);
                }
            }
        });

        //========================  STOP Button code ===============================================
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enabling the stop button and disabling the stop button for reflecting the activity state
                activityNameDisplay.setText(nameRender + ": Inactive");
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);


                String eventID = SetAlarmManager.getActiveScheduleEvent(getApplicationContext());

                if (eventID != null) {
                    ScheduleEventDao scheduleEventDao = AppDatabase.getAppDatabase(getApplicationContext()).scheduleEventDao();
                    SpontaneousEventDao spontaneousEventDao = AppDatabase.getAppDatabase(getApplicationContext()).spontaneousEventDao();
                    if (scheduleEventDao.findScheduleEventById(eventID) != null) {
                        SetAlarmManager.endEventEarly(getApplicationContext(), eventID);
                        return;
                    } else if (spontaneousEventDao.findSpontaneousEventById(eventID) != null) {
                        //Disable all the rules
                    }


                }

                //sets the myTotalSteps variable with current step count
                new ViewTodaysStepCountTask().execute();
                try {
                    //Added delay of 2.5 seconds to make an API call and fetch stepcount information
                    Thread.sleep(2500);
                }
                catch (Exception e) {

                }

                // Pulling the most recent event from the spontaneous activity table
                SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();

                //Fetching the starting value of the step count to calculate the actual steps covered
                myStartStepsString = mostRecentEvent.getStartValue();
                //Log.d("MYTAG", myStartStepsString);
                myStartSteps = Integer.parseInt(myStartStepsString);

                //Fetching the ending value of the step count to calculate the actual steps covered
                myCurrentSteps = Integer.parseInt(myTotalSteps);
                //Log.d("MYTAG", Integer.toString(myStartSteps));


                //Calculating the final step count
                myFinalSteps = myCurrentSteps - myStartSteps;
                myFinalStepsString = Integer.toString(myFinalSteps);

                //Updating the most recent event with - endTime, endValue, finalValue and Active flag status
                mostRecentEvent.setEndTime(Calendar.getInstance());
                mostRecentEvent.setEndValue(myTotalSteps);
                mostRecentEvent.setFinalValue(myFinalStepsString);
                mostRecentEvent.setActive(false);
                db.spontaneousEventDao().update(mostRecentEvent);

                //Setting the SpontaneousActiveStepCounter textView
                spontaneousActiveStepCounter = findViewById(R.id.SpontaneousActiveStepCounter);
                if (nameRender.equals("Running")) {
                    spontaneousActiveStepCounter.setText("Step Count: " + myFinalStepsString);
                    spontaneousActiveStepCounter.setTextSize(20);
                }


                //printData();
            }
        });

        //========================  View History Button code ===============================================
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpontaneousActive.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    } //End of OnCreate


    //For debugging purposes - Displays the Final step count
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


    //Parses the result string to get the stepcount
    private void showDataSet(DataSet dataSet) {
        //Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        //DateFormat dateFormat = DateFormat.getDateInstance();
        //DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            //Log.e("History", "Data point:");
            //Log.e("History", "\tType: " + dp.getDataType().getName());
            //Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            //Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
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

    //Method making the API call using the created GoogleApiClient
    private void displayStepDataForToday() {
        try {
            //makes a call to the History API (One of the fitness APIs) to get the Daily total
            DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
            showDataSet(result.getTotal());
        }
        catch (Exception e) {
            
        }
    }

    //Async task initiating the call to the Fitness History API
    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public void checkFitStatus()
    {
        boolean isAppInstalled = appInstalledOrNot("com.google.android.apps.fitness");

        if(isAppInstalled) {
            //checkInstallation.setText("Fit is installed");
        }
        else {
            //checkInstallation.setText("Fit is not installed");
            Intent intent = new Intent(SpontaneousActive.this, GoogleFitPopUp.class);
            intent.putExtra("FitInstalled", "no");
            startActivity(intent);
        }
    }
}

