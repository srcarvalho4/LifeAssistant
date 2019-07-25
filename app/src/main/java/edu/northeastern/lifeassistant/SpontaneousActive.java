package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

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
import utils.DisplayRule;
import utils.RuleAdapter;
import utils.RuleAdapterItem;

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
        }
        else {
            nameRender += ": Inactive";
        }

        activityNameDisplay = findViewById(R.id.SpontaneousActiveName);
        LinearLayout linearLayout = findViewById(R.id.SpontaneousActiveColorArea);

        activityNameDisplay.setText(nameRender);
        linearLayout.setBackgroundColor(color);

        listView = findViewById(R.id.SpontaneousActiveList);

        RuleAdapter adapter = new RuleAdapter(this, rules);

        listView.setAdapter(adapter);

//        progressBar = findViewById(R.id.spontaneousProgress);
//        progressBar.setProgress(progress);
//        progressBar.setMax(100);
//        timer = new Timer();
    }

//    private class ProgressIncrement extends TimerTask {
//
//        @Override
//        public void run() {
//            progress += 1;
//            progressBar.setProgress(progress);
//        }
//    }

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

//    public void startButtonClick(View view) {
//        task = new ProgressIncrement();
//        if (!isRunning) {
//            isRunning = true;
//            timer.schedule(task, 0, 1000);
//            progressBar.setIndeterminate(true);
//        } else {
//            Toast.makeText(this, "Activity already started.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void stopButtonClick(View view) {
//        if (isRunning) {
//            isRunning = false;
//            task.cancel();
//            timer.purge();
//            progress = 0;
//            //progressBar.setProgress(progress);
//            progressBar.setIndeterminate(false);
//        } else {
//            Toast.makeText(this, "No Activity running to stop.", Toast.LENGTH_SHORT).show();
//        }
//    }

}
