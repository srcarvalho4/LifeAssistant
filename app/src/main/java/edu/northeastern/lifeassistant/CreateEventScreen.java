package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.dpro.widgets.WeekdaysPicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import utils.ScheduleEvent;

public class CreateEventScreen extends AppCompatActivity {

    private AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());

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
        List<ActivityDb> activityDb = AppDatabase.getAppDatabase(getApplicationContext()).activityDao().findAllActivities();
        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(activityDb.get(i).getName());
        }

        // Get widget references
        TextView title = findViewById(R.id.editActivityTitle);
        eventNameEditText = findViewById(R.id.createEventNameEditText);
        activitySpinner = findViewById(R.id.createEventActivitySpinner);
        weekdaysPicker = findViewById(R.id.createEventDayPicker);
        eventStartTimeEditText = findViewById(R.id.createEventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.createEventEndTimeEditText);
        cancelButton = findViewById(R.id.createEventCancelButton);
        saveButton = findViewById(R.id.createEventSaveButton);



        if (getIntent().getBooleanExtra("edit", false)) {
            String eventName = getIntent().getStringExtra("name");
            ScheduleEvent myEvent = new ScheduleEvent(getApplicationContext(), eventName);
            title.setText("Edit Activity");
            eventNameEditText.setText(myEvent.getName());
            eventStartTimeEditText.setText(myEvent.getStartTimeText());
            eventEndTimeEditText.setText(myEvent.getEndTimeText());
            weekdaysPicker.setSelectedDays(myEvent.getDayData());
        }
        else {
            title.setText("Create Activity");
        }



        // Add activity list to spinner
        activitySpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, activities));

        // Clear DayPicker default selections
        weekdaysPicker.setSelectedDays(new ArrayList<>());

        // Show TimePicker onClick
        eventStartTimeEditText.setOnClickListener(view -> showTimePicker(eventStartTimeEditText));

        // Show TimePicker onClick
        eventEndTimeEditText.setOnClickListener(view -> showTimePicker(eventEndTimeEditText));

        // Clear all selections onClick
        cancelButton.setOnClickListener(view -> {
            eventNameEditText.getText().clear();
            activitySpinner.setSelection(0);
            weekdaysPicker.setSelectedDays(new ArrayList<Integer>());
            weekdaysPicker.setEditable(false);
            eventStartTimeEditText.getText().clear();
            eventEndTimeEditText.getText().clear();
        });

        // Save event onClick
        saveButton.setOnClickListener(view -> {
        });

    }

    private void showTimePicker(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateEventScreen.this, (timePicker, hourOfDay, minutes) -> {
                    Calendar newTime = Calendar.getInstance();
                    newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    newTime.set(Calendar.MINUTE, minutes);
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
                    editText.setText(timeFormatter.format(newTime.getTime()));
                }, 0, 0, false);
        timePickerDialog.show();
    }

}
