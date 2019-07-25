package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import utils.Activity;
import utils.ActivityAdapter;
import utils.DrivingModeRule;
import utils.NightModeRule;
import utils.RingerRule;
import utils.Rule;

public class SpontaneousScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_screen);

        listView = findViewById(R.id.SpontaneousScreenList);

        db = AppDatabase.getAppDatabase(getApplicationContext());


        List<ActivityDb> activityDb = new ArrayList<>();

        activityDb = db.activityDao().findAllActivities();

        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }
        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SpontaneousScreen.this, SpontaneousActive.class);

                intent.putExtra("name", activities.get(i).getName());
                intent.putExtra("color", activities.get(i).getColor());
                intent.putExtra("location", "Spontaneous");
                startActivity(intent);
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
            case VOLUME: return new RingerRule(rule.getSettingValue());
            default: throw new IllegalArgumentException("need a valid state type");
        }
    }
}
