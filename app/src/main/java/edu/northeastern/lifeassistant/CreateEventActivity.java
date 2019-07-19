package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import com.dpro.widgets.WeekdaysPicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private List<String> activities = new ArrayList<>();
    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private WeekdaysPicker weekdaysPicker;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;
    private Button cancelButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Temporary spinner list
        activities.add("");
        activities.add("Class");
        activities.add("Running");
        activities.add("Reading");
        activities.add("Driving");
        activities.add("Sleeping");

        // Get widget references
        eventNameEditText = findViewById(R.id.createEventNameEditText);
        activitySpinner = findViewById(R.id.createEventActivitySpinner);
        weekdaysPicker = findViewById(R.id.createEventDayPicker);
        eventStartTimeEditText = findViewById(R.id.createEventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.createEventEndTimeEditText);
        cancelButton = findViewById(R.id.createEventCancelButton);
        saveButton = findViewById(R.id.createEventSaveButton);

        // Add activity list to spinner
        activitySpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, activities));

        // Clear DayPicker default selections
        weekdaysPicker.setSelectedDays(new ArrayList<Integer>());

        // Show TimePicker onClick
        eventStartTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(eventStartTimeEditText);
            }
        });

        // Show TimePicker onClick
        eventEndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(eventEndTimeEditText);
            }
        });

        // Clear all selections onClick
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventNameEditText.getText().clear();
                activitySpinner.setSelection(0);
                weekdaysPicker.setSelectedDays(new ArrayList<Integer>());
                weekdaysPicker.setEditable(false);
                eventStartTimeEditText.getText().clear();
                eventEndTimeEditText.getText().clear();
            }
        });

        // Save event onClick
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showTimePicker(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                java.util.Calendar newTime = java.util.Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minutes);
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
                editText.setText(timeFormatter.format(newTime.getTime()));
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }

}
