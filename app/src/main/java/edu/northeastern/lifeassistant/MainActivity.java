package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.northeastern.lifeassistant.db.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
