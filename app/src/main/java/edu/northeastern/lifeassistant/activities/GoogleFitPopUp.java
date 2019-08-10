package edu.northeastern.lifeassistant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.spontaneous.SpontaneousScreen;

public class GoogleFitPopUp extends AppCompatActivity {

    Button googleFitDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String selectLayout = getIntent().getStringExtra("FitInstalled");

        if (selectLayout.equals("no")) {
            setContentView(R.layout.activity_google_fit_pop_up);
            googleFitDownload = findViewById(R.id.googleFitDownloadButton);
            googleFitDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness&hl=en_US"));
                    startActivity(implicitIntent);
                }
            });

        }
        else {
            setContentView(R.layout.activity_google_fit_pop_up2);
        }

        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);

        //int width = dm.widthPixels;
        //int height = dm.heightPixels;

        //getWindow().setLayout((int) (width*.8), (int) (height*.6));


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SpontaneousScreen.class);
        intent.putExtra("location", "Spontaneous");
        startActivity(intent);
    }
}
