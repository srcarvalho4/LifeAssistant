package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import utils.Activity;
import utils.RuleAdapter;
import utils.RuleAdapterItem;
import utils.SetAlarmManager;

public class SpontaneousActive extends AppCompatActivity {

    ProgressBar progressBar;
    Timer timer;
    Boolean isRunning;
    String start = "Start";
    String stop = "Stop";
    int progress;
    TextView activityNameDisplay;
    String activityName;
    Button button;
//    ProgressIncrement task;

    ListView listView;
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_active);

//        task = new ProgressIncrement();

        isRunning = false;
        button = findViewById(R.id.SpontaneousActiveButton);

//        progress = 0;

        Activity myActivity = new Activity(getApplicationContext(), getIntent().getStringExtra("name"));

        for (int i = 0; i < myActivity.getRules().size(); i++) {
            rules.add(new RuleAdapterItem(myActivity.getRules().get(i)));
        }

        activityName = myActivity.getName();
        int color = myActivity.getColor();

        String nameRender = activityName;
        if (isRunning) {
            nameRender += ": Active";
        } else {
            nameRender += ": Inactive";
        }

        activityNameDisplay = findViewById(R.id.SpontaneousActiveName);
        LinearLayout linearLayout = findViewById(R.id.SpontaneousActiveColorArea);

        activityNameDisplay.setText(nameRender);
        linearLayout.setBackgroundColor(color);

        listView = findViewById(R.id.SpontaneousActiveList);

        RuleAdapter adapter = new RuleAdapter(this, rules, false);

        listView.setAdapter(adapter);
    }

    public void toggleActive(View view) {

        String nameRender = activityName;

        if (!isRunning) {
            isRunning = true;
            button.setText(stop);
            nameRender += ": Active";
            activityNameDisplay.setText(nameRender);
        }
        else {
            isRunning = false;
            button.setText(start);
            nameRender += ": Inactive";
            activityNameDisplay.setText(nameRender);
        }
    }


    @Override
    public void onBackPressed() {
        if (SetAlarmManager.getActiveScheduleEvent(getApplicationContext()) == null) {
            Intent intent = new Intent(getApplicationContext(), SpontaneousScreen.class);
            intent.putExtra("location", "Spontaneous");
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
            intent.putExtra("location", "Activity");
            startActivity(intent);
        }
    }
}
