package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private TextView eventStartTimeTextView;
    private TextView eventEndTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        activitySpinner = findViewById(R.id.activitySpinner);
        eventStartTimeTextView = findViewById(R.id.eventStartTimeEditText);
        eventEndTimeTextView = findViewById(R.id.eventEndTimeEditText);

        eventStartTimeTextView.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_SHORT).show();
            }
        });

//        eventStartTimeTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}
