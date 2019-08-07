package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class CheckGoogleFitInstalled extends AppCompatActivity {

    TextView checkInstallation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_google_fit_installed);
        checkInstallation = findViewById(R.id.checkFitInstallationTextView);

        boolean isAppInstalled = appInstalledOrNot("com.google.android.apps.fitness");

        if(isAppInstalled) {
            checkInstallation.setText("Fit is installed");
        }
        else {
            checkInstallation.setText("Fit is not installed");
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
}
