package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.dpro.widgets.WeekdaysPicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class CreateEventScreen extends AppCompatActivity {

    private AppDatabase db;

    private TextView titleTextView;
    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private WeekdaysPicker weekdaysPicker;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;
    private Button cancelButton;
    private Button saveButton;

    private boolean isEdit;

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        // Get widget references
        titleTextView = findViewById(R.id.editActivityTitle);
        eventNameEditText = findViewById(R.id.createEventNameEditText);
        activitySpinner = findViewById(R.id.createEventActivitySpinner);
        weekdaysPicker = findViewById(R.id.createEventDayPicker);
        eventStartTimeEditText = findViewById(R.id.createEventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.createEventEndTimeEditText);
        cancelButton = findViewById(R.id.createEventCancelButton);
        saveButton = findViewById(R.id.createEventSaveButton);

        // Set widgets to selected event values if isEdit
        isEdit = getIntent().getBooleanExtra("edit", false);
        setWidgets(isEdit);

        // Add activity list to spinner
        List<String> spinnerItems = new ArrayList<>();
        db.activityDao().findAllActivities().forEach(a -> spinnerItems.add(a.getName()));
        activitySpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerItems));

        // Clear DayPicker default selections
        weekdaysPicker.setSelectedDays(new ArrayList<>());

        // Show TimePicker onClick
        eventStartTimeEditText.setOnClickListener(view -> showTimePicker(eventStartTimeEditText));

        // Show TimePicker onClick
        eventEndTimeEditText.setOnClickListener(view -> showTimePicker(eventEndTimeEditText));

        // Redirect to ScheduleScreen onClick
        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScheduleScreen.class);
            startActivity(intent);
        });

        // Save event onClick
        saveButton.setOnClickListener(view -> {
            saveOrUpdateScheduleEvent(isEdit);
            Intent intent = new Intent(this, ScheduleScreen.class);
            startActivity(intent);
        });
    }

    private void setWidgets(boolean isEdit) {
        if (isEdit) {
            String eventName = getIntent().getStringExtra("name");
            ScheduleEventDb currentEvent = db.scheduleEventDao().findScheduleEventByName(eventName);
            titleTextView.setText("Edit Event");
            eventNameEditText.setText(currentEvent.getName());
            eventStartTimeEditText.setText(timeFormatter.format(currentEvent.getStartTime().getTime()));
            eventEndTimeEditText.setText(timeFormatter.format(currentEvent.getEndTime().getTime()));
            weekdaysPicker.setSelectedDays(currentEvent.getDaysOfWeek());
        }
        else {
            titleTextView.setText("Create Event");
        }
    }

    private void saveOrUpdateScheduleEvent(boolean isEdit) {
        String selectedActivityName = activitySpinner.getSelectedItem().toString();
        String selectedActivityId = db.activityDao().findActivityByName(selectedActivityName).getId();
        String eventName = eventNameEditText.getText().toString();
        String eventStartTimeString = eventStartTimeEditText.getText().toString();
        String eventEndTimeString = eventEndTimeEditText.getText().toString();
        Calendar eventStartTime = Calendar.getInstance();
        Calendar eventEndTime = Calendar.getInstance();
        List<Integer> eventDays = weekdaysPicker.getSelectedDays();

        try {
            eventStartTime.setTime(timeFormatter.parse(eventStartTimeString));
            eventEndTime.setTime(timeFormatter.parse(eventEndTimeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(isEdit) {
            ScheduleEventDb scheduleEventDb = db.scheduleEventDao()
                    .findScheduleEventByName(getIntent().getStringExtra("name"));
            scheduleEventDb.setActivityId(selectedActivityId);
            scheduleEventDb.setName(eventName);
            scheduleEventDb.setStartTime(eventStartTime);
            scheduleEventDb.setEndTime(eventEndTime);
            scheduleEventDb.setDaysOfWeek(eventDays);
            db.scheduleEventDao().update(scheduleEventDb);
        } else {
            ScheduleEventDb scheduleEventDb = new ScheduleEventDb(selectedActivityId, eventName,
                    eventStartTime, eventEndTime, eventDays);
            db.scheduleEventDao().insert(scheduleEventDb);
        }
    }

    private void showTimePicker(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateEventScreen.this, (timePicker, hourOfDay, minutes) -> {
            Calendar newTime = Calendar.getInstance();
            newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            newTime.set(Calendar.MINUTE, minutes);
            editText.setText(timeFormatter.format(newTime.getTime()));
        }, 0, 0, false);
        timePickerDialog.show();
    }

}
