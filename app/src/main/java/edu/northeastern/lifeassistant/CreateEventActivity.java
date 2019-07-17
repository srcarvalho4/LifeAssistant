package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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

        eventStartTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TimePickerDialog timePickerDialog = new TimePickerDialog(
//                        CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//
//                    }
//                }, 0, 0, false);
//                timePickerDialog.show();
                Toast.makeText(getApplicationContext(),"Hello Javatpoint", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
