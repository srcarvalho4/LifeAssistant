package edu.northeastern.lifeassistant.activities.spontaneous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.activities.GoogleFitPopUp;
import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.SplashScreen;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.utils.items.Activity;
import edu.northeastern.lifeassistant.utils.adapters.ActivityAdapter;
import edu.northeastern.lifeassistant.utils.rules.DrivingModeRule;
import edu.northeastern.lifeassistant.utils.rules.NightModeRule;
import edu.northeastern.lifeassistant.utils.rules.RingerRule;
import edu.northeastern.lifeassistant.utils.rules.Rule;

public class SpontaneousScreen extends AppCompatActivity {

    GridView gridView;

    AppDatabase db;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_screen);

        gridView = findViewById(R.id.SpontaneousScreenGrid);

        db = AppDatabase.getAppDatabase(getApplicationContext());


        final List<ActivityDb> activityDb = db.activityDao().findAllActivities();

        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }
        ActivityAdapter adapter = new ActivityAdapter(this, activities);


        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(SpontaneousScreen.this, SpontaneousActive.class);

                intent.putExtra("name", activityDb.get(i).getId());
                intent.putExtra("location", "Spontaneous");
                checkFitStatus(intent);
            }
        });
    }

    private ArrayList<Rule> getRules(String activityID) {
        //1) get ActivityDB from data base
        //2) get list of rule IDs
        //3) for each list of rules, switch on setting type + create appropriate one with value
        //4) add to list of rules and return

        ArrayList<Rule> rules = new ArrayList<>();

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
            case RINGER: return new RingerRule(getApplicationContext(), rule.getSettingValue());
            default: throw new IllegalArgumentException("need a valid state type");
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

    public void checkFitStatus(Intent intent)
    {
        boolean isAppInstalled = appInstalledOrNot("com.google.android.apps.fitness");

        if(isAppInstalled) {
            //checkInstallation.setText("Fit is installed");
            startActivity(intent);
        }
        else {
            //checkInstallation.setText("Fit is not installed");
            Intent myIntent = new Intent(SpontaneousScreen.this, GoogleFitPopUp.class);
            myIntent.putExtra("FitInstalled", "no");
            startActivity(myIntent);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SpontaneousScreen.this, SplashScreen.class);
        startActivity(intent);
    }
}
