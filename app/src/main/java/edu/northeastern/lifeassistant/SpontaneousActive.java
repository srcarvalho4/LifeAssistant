package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import utils.DisplayRule;
import utils.RuleAdapter;
import utils.RuleAdapterItem;

public class SpontaneousActive extends AppCompatActivity {

    ProgressBar progressBar;
    Timer timer;
    Boolean isRunning;
    int progress;
    ProgressIncrement task;

    ListView listView;
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_active);

        task = new ProgressIncrement();

        progress = 0;

        String name = getIntent().getStringExtra("name");
        int color = getIntent().getIntExtra("color", Color.WHITE);

        TextView textView = findViewById(R.id.SpontaneousActiveName);
        LinearLayout linearLayout = findViewById(R.id.SpontaneousActiveColorArea);

        textView.setText(name);
        linearLayout.setBackgroundColor(color);

        populateList();
        listView = findViewById(R.id.SpontaneousActiveList);

        RuleAdapter adapter = new RuleAdapter(this, rules);

        listView.setAdapter(adapter);

        progressBar = findViewById(R.id.spontaneousProgress);
        progressBar.setProgress(progress);
        progressBar.setMax(100);

        isRunning = false;
        timer = new Timer();
    }

    private class ProgressIncrement extends TimerTask {

        @Override
        public void run() {
            progress += 1;
            progressBar.setProgress(progress);
        }
    }

    public void startButtonClick(View view) {
        task = new ProgressIncrement();
        if (!isRunning) {
            isRunning = true;
            timer.schedule(task, 0, 1000);
            progressBar.setIndeterminate(true);
        } else {
            Toast.makeText(this, "Activity already started.", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopButtonClick(View view) {
        if (isRunning) {
            isRunning = false;
            task.cancel();
            timer.purge();
            progress = 0;
            //progressBar.setProgress(progress);
            progressBar.setIndeterminate(false);
        } else {
            Toast.makeText(this, "No Activity running to stop.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateList() {
        rules.add(new RuleAdapterItem(new DisplayRule("Do Not Disturb Mode")));
        rules.add(new RuleAdapterItem(new DisplayRule("Sound")));
        rules.add(new RuleAdapterItem(new DisplayRule("Location")));
    }
}
