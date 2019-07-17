package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SpontaneousActive extends AppCompatActivity {

    ProgressBar progressBar;
    Timer timer;
    Boolean isRunning;
    int progress;
    ProgressIncrement task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_active);

        task = new ProgressIncrement();

        progress = 0;

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
}
