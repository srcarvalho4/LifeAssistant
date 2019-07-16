package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        activitySpinner = findViewById(R.id.activitySpinner);
        eventStartTimeEditText = findViewById(R.id.eventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.eventEndTimeEditText);

    }

}
