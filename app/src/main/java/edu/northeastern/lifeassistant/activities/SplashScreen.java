package edu.northeastern.lifeassistant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.activity.ActivityScreen;
import edu.northeastern.lifeassistant.activities.spontaneous.SpontaneousActive;
import edu.northeastern.lifeassistant.utils.services.SetAlarmManager;

public class SplashScreen extends AppCompatActivity {

    Button getStarted;
    ImageView balloon;
    Animation frombottom, fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getStarted = findViewById(R.id.getStarted);
        balloon = findViewById(R.id.balloon);
        //Adding the frombottom animation layout to getStarted button
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombotton);
        getStarted.setAnimation(frombottom);

        //Adding the fromtop animation layout to balloon image
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        balloon.setAnimation(fromtop);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                String activityId = SetAlarmManager.getActiveActivity(getApplicationContext());
                if (activityId == null) {
                    intent = new Intent(SplashScreen.this, ActivityScreen.class);
                }
                else {
                    intent = new Intent(SplashScreen.this, SpontaneousActive.class);
                    intent.putExtra("name", activityId);
                    intent.putExtra("location", "Spontaneous");
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
