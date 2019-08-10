package edu.northeastern.lifeassistant.activities.event;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.dpro.widgets.WeekdaysPicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.utils.services.SetAlarmManager;

public class CreateEventScreen extends AppCompatActivity {

    private AppDatabase db;

    private TextView titleTextView;
    private EditText eventNameEditText;
    private Spinner activitySpinner;
    private WeekdaysPicker weekdaysPicker;
    private Switch eventReminderSwitch;
    private EditText eventStartTimeEditText;
    private EditText eventEndTimeEditText;
    private Button deleteButton;
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
        deleteButton = findViewById(R.id.createEventDeleteButton);
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

        // Delete event and redirect onClick
        deleteButton.setOnClickListener(view -> {
            ScheduleEventDb eventDb = db.scheduleEventDao().findScheduleEventById(selectedEventId);
            SetAlarmManager.cancelAllPending(this, eventDb);
            db.scheduleEventDao().deleteScheduleEventsById(selectedEventId);
            Intent intent = new Intent(getApplicationContext(), ScheduleScreen.class);
            intent.putExtra("location", "Schedule");
            startActivity(intent);
        });

        // Save event onClick
        saveButton.setOnClickListener(view -> {
            String errorMsg = null;

            Calendar eventStartTime = Calendar.getInstance();
            Calendar eventEndTime = Calendar.getInstance();

            if(eventNameIsValid()) {
                if(daysIsValid()) {
                    if(timePeriodIsValid()) {
                        try {
                            eventStartTime.setTime(timeFormatter.parse(eventStartTimeEditText.getText().toString()));
                            eventEndTime.setTime(timeFormatter.parse(eventEndTimeEditText.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(isScheduleConflict(eventStartTime, eventEndTime)) {
                            saveOrUpdateScheduleEvent(isEdit);
                            Intent intent = new Intent(this, ScheduleScreen.class);
                            intent.putExtra("location", "Schedule");
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Time Conflict With Another Event", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        errorMsg = "Invalid Time Period";
                    }
                } else {
                    errorMsg = "Select Days";
                }
            } else {
                errorMsg = "Invalid Event Name";
            }

            if (errorMsg != null) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        // Set widgets to selected event values if isEdit
        setWidgets(isEdit);
        
        ImageButton backButton = findViewById(R.id.createEventBackButton);
        // Abort and redirect onClick
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleScreen.class);
                intent.putExtra("location", "Schedule");
                startActivity(intent);
            }
        });
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
            Log.d("setAlarm", "calling setSchedulingAlarm");
            SetAlarmManager.cancelAllPending(this, scheduleEventDb);
            SetAlarmManager.setSchedulingAlarm(this, scheduleEventDb.getId());
        } else {
            ScheduleEventDb scheduleEventDb = new ScheduleEventDb(selectedActivityId, eventName,
                    eventStartTime, eventEndTime, eventDays, reminderSwitchState);
            db.scheduleEventDao().insert(scheduleEventDb);
            Log.d("setAlarm", "calling setSchedulingAlarm");
            SetAlarmManager.setSchedulingAlarm(this, scheduleEventDb.getId());
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

    private boolean eventNameIsValid() {
        String eventName = eventNameEditText.getText().toString();
        if(!eventName.isEmpty()) {
            List<String> existingEventNames = new ArrayList<>();
            db.scheduleEventDao().findAllScheduleEvents().forEach(e -> existingEventNames.add(e.getName()));

            if(isEdit) {
                String oldEventName = db.scheduleEventDao().findScheduleEventById(selectedEventId).getName();
                if(eventName.equals(oldEventName)) {
                    return true;
                }
            }
            return !existingEventNames.contains(eventName);
        }
        return false;
    }

    private boolean daysIsValid() {
        return !weekdaysPicker.getSelectedDays().isEmpty();
    }

    private boolean timePeriodIsValid() {
        String startTimeText = eventStartTimeEditText.getText().toString();
        String endTimeText = eventEndTimeEditText.getText().toString();

        if(!startTimeText.isEmpty() && !endTimeText.isEmpty()) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();

            try {
                startTime.setTime(timeFormatter.parse(eventStartTimeEditText.getText().toString()));
                endTime.setTime(timeFormatter.parse(eventEndTimeEditText.getText().toString()));
            } catch (ParseException e) {
                return false;
            }

            if(startTime.getTimeInMillis() < endTime.getTimeInMillis()) {
                return true;
            }
        }

        return false;
    }

    private boolean isScheduleConflict(Calendar startTime, Calendar endTime) {
        return db.scheduleEventDao().findConflicts(startTime, endTime, selectedEventId).isEmpty();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ScheduleScreen.class);
        intent.putExtra("location", "Schedule");
        startActivity(intent);
    }
}
