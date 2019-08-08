package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckGoogleFitInstalled extends AppCompatActivity {

    TextView checkInstallation;
    Button checkFitStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_google_fit_installed);
        checkInstallation = findViewById(R.id.checkFitInstallationTextView);
        checkFitStatus = findViewById(R.id.checkFitStatusButton);

        boolean isAppInstalled = appInstalledOrNot("com.google.android.apps.fitness");

        if(isAppInstalled) {
            checkInstallation.setText("Fit is installed");
            Intent intent = new Intent(CheckGoogleFitInstalled.this, GoogleFitPopUp.class);
            startActivity(intent);
        }
        else {
            checkInstallation.setText("Fit is not installed");
            Intent intent = new Intent(CheckGoogleFitInstalled.this, GoogleFitPopUp.class);
            startActivity(intent);
        }

        checkFitStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckGoogleFitInstalled.this, GoogleFitPopUp.class);
                startActivity(intent);
            }
        });
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

    private void downloadFit() {
        //Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness&hl=en_US"));
        //startActivity(implicitIntent);
    }

}
