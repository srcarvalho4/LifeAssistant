package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.dpro.widgets.WeekdaysPicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

public class CreateEventScreen extends AppCompatActivity {

    private AppDatabase db;

    private TextView titleTextView;
    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private WeekdaysPicker weekdaysPicker;
    private Switch eventReminderSwitch;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;
    private Button cancelButton;
    private Button saveButton;

    private boolean isEdit;
    private String selectedEventId;
    private List<String> spinnerItems = new ArrayList<>();

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
        eventReminderSwitch = findViewById(R.id.createEventReminderSwitch);
        eventStartTimeEditText = findViewById(R.id.createEventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.createEventEndTimeEditText);
        cancelButton = findViewById(R.id.createEventCancelButton);
        saveButton = findViewById(R.id.createEventSaveButton);

        // Get extras
        selectedEventId = getIntent().getStringExtra("eventId");
        isEdit = getIntent().getBooleanExtra("edit", false);

        // Add activity list to spinner
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

        // Set widgets to selected event values if isEdit
        setWidgets(isEdit);
    }

    private void setWidgets(boolean isEdit) {
        if (isEdit) {
            ScheduleEventDb currentEvent = db.scheduleEventDao().findScheduleEventById(selectedEventId);
            ActivityDb currentActivity = db.activityDao().findActivityByEventId(selectedEventId);
            titleTextView.setText("Edit Event");
            eventNameEditText.setText(currentEvent.getName());
            activitySpinner.setSelection(spinnerItems.indexOf(currentActivity.getName()));
            eventStartTimeEditText.setText(timeFormatter.format(currentEvent.getStartTime().getTime()));
            eventEndTimeEditText.setText(timeFormatter.format(currentEvent.getEndTime().getTime()));
            eventReminderSwitch.setChecked(currentEvent.getReminderSwitchState());
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
        Boolean reminderSwitchState = eventReminderSwitch.isChecked();
        List<Integer> eventDays = weekdaysPicker.getSelectedDays();
        String eventStartTimeString = eventStartTimeEditText.getText().toString();
        String eventEndTimeString = eventEndTimeEditText.getText().toString();
        Calendar eventStartTime = Calendar.getInstance();
        Calendar eventEndTime = Calendar.getInstance();

        try {
            eventStartTime.setTime(timeFormatter.parse(eventStartTimeString));
            eventEndTime.setTime(timeFormatter.parse(eventEndTimeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(isEdit) {
            ScheduleEventDb scheduleEventDb = db.scheduleEventDao().findScheduleEventById(selectedEventId);
            scheduleEventDb.setActivityId(selectedActivityId);
            scheduleEventDb.setName(eventName);
            scheduleEventDb.setStartTime(eventStartTime);
            scheduleEventDb.setEndTime(eventEndTime);
            scheduleEventDb.setReminderSwitchState(reminderSwitchState);
            scheduleEventDb.setDaysOfWeek(eventDays);
            db.scheduleEventDao().update(scheduleEventDb);
        } else {
            ScheduleEventDb scheduleEventDb = new ScheduleEventDb(selectedActivityId, eventName,
                    eventStartTime, eventEndTime, eventDays, reminderSwitchState);
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
