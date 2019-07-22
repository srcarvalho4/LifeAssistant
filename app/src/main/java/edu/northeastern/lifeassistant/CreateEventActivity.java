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
import android.widget.Toast;
import com.dpro.widgets.WeekdaysPicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import edu.northeastern.lifeassistant.db.AppDatabase;

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

//        // TODO: REMOVE
//        // TESTS
        AppDatabase db = AppDatabase.getAppDatabase(this);

        Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();

//        edu.northeastern.lifeassistant.db.settingDao().insert(new Setting(SettingType.AIRPLANE_MODE));
//        List<Setting> s = edu.northeastern.lifeassistant.db.settingDao().findAllSettings();
//        for(Setting se : s) {
//            edu.northeastern.lifeassistant.db.settingDao().delete(se);
//        }

//        edu.northeastern.lifeassistant.db.activityDao().insert(new Activity("test", ColorType.RED));
//        List<Activity> a = edu.northeastern.lifeassistant.db.activityDao().findAllActivities();
//        for(Activity ac : a) {
//            edu.northeastern.lifeassistant.db.activityDao().delete(ac);
//        }

//        Long aId = edu.northeastern.lifeassistant.db.activityDao().insert(new Activity("test", ColorType.RED));
//        Long sId = edu.northeastern.lifeassistant.db.settingDao().insert(new Setting(SettingType.AIRPLANE_MODE));
//        edu.northeastern.lifeassistant.db.ruleDao().insert(new Rule(aId, sId, false, "0"));
//        Rule r = edu.northeastern.lifeassistant.db.ruleDao().findRuleById(new Long(1));
//        edu.northeastern.lifeassistant.db.ruleDao().delete(r);

//        // Delete Activities
//        List<Activity> a = edu.northeastern.lifeassistant.db.activityDao().findAllActivities();
//        for(Activity ac : a) {
//            edu.northeastern.lifeassistant.db.activityDao().delete(ac);
//        }

//        // Delete Settings
//        List<Setting> s = edu.northeastern.lifeassistant.db.settingDao().findAllSettings();
//        for(Setting se : s) {
//            edu.northeastern.lifeassistant.db.settingDao().delete(se);
//        }

//        // Delete rules
//        List<Rule> r = edu.northeastern.lifeassistant.db.ruleDao().findAllRules();
//        for(Rule ru : r) {
//            edu.northeastern.lifeassistant.db.ruleDao().delete(ru);
//        }

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
