package edu.northeastern.lifeassistant.utils.rules;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;
import edu.northeastern.lifeassistant.db.types.SettingType;

public class StepCounterRule implements Rule, GoogleApiClient.ConnectionCallbacks,
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

    Context context;

    public StepCounterRule(Context context) {

        this.context = context;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage((FragmentActivity) context, 0, this)
                .build();
    }



    @Override
    public void enable() {
        new ViewTodaysStepCountTask().execute();
        try {
            Thread.sleep(2500);
        }
        catch (Exception e) {

        }
        AppDatabase db = AppDatabase.getAppDatabase(context);
        ActivityDb activity = db.activityDao().findActivityByName("Running");
        //progressBar.setVisibility(View.INVISIBLE);
        SpontaneousEventDb sEvent = new SpontaneousEventDb(activity.getId(), null, myTotalSteps, null, null);
        db.spontaneousEventDao().insert(sEvent);
        SpontaneousEventDb mostRecentEvent = db.spontaneousEventDao().findMostRecentEvent();
        mostRecentEvent.setEndTime(Calendar.getInstance());
        mostRecentEvent.setEndValue(null);

    }

    @Override
    public void disable() {
        new ViewTodaysStepCountTask().execute();
        try {
            Thread.sleep(2500);
        }
        catch (Exception e) {

        }
        AppDatabase db = AppDatabase.getAppDatabase(context);
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

        db.spontaneousEventDao().update(mostRecentEvent);
        printData();
    }

    @Override
    public String getName() {
        return SettingType.STEP_COUNT.getValue();
    }

    @Override
    public List<Pair<Integer, String>> getSettingValues() {
        return null;
    }

    @Override
    public int getSetting() {
        return 0;
    }


    public void printData() {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        SpontaneousEventDb check = db.spontaneousEventDao().findMostRecentEvent();
        Calendar endtime = check.getEndTime();
        String endValue = check.getEndValue();
        String startValue = check.getStartValue();
        Calendar startTime = check.getStartTime();
        String finalValue = check.getFinalValue();


        String printString = "Start Time: " + startTime.getTime().toString() + "\nEnd Time: " + endtime.getTime().toString() +
                "\nStep Count" + finalValue;

        Toast.makeText(context, printString, Toast.LENGTH_LONG).show();



        Log.d("Steps", startTime.getTime().toString());
        Log.d("Steps", startValue);
        Log.d("Steps", endtime.getTime().toString());
        Log.d("Steps", endValue);
        Log.d("Steps", check.getActivityId().toString());
        Log.d("Steps", finalValue);

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
