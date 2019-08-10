package edu.northeastern.lifeassistant.activities.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import android.app.NotificationManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.types.SettingType;
import edu.northeastern.lifeassistant.utils.items.Activity;
import edu.northeastern.lifeassistant.utils.adapters.ColorAdapter;
import edu.northeastern.lifeassistant.utils.items.ColorPicker;
import edu.northeastern.lifeassistant.utils.rules.DrivingModeRule;
import edu.northeastern.lifeassistant.utils.rules.NightModeRule;
import edu.northeastern.lifeassistant.utils.rules.RingerRule;
import edu.northeastern.lifeassistant.utils.rules.Rule;
import edu.northeastern.lifeassistant.utils.adapters.RuleAdapter;
import edu.northeastern.lifeassistant.utils.items.RuleAdapterItem;

public class CreateActivityScreen extends AppCompatActivity {

    private AppDatabase db;

    private TextView titleTextView;
    private EditText activityNameEditText;
    private Button addRuleButton;
    private Button saveButton;
    private Button deleteButton;
    private ImageButton backButton;
    private ListView ruleListView;

    private RuleAdapter ruleAdapter;
    private ColorAdapter colorAdapter;

    private Set<RuleAdapterItem> rulesSet;
    private ArrayList<String> rulesMenuItems = new ArrayList<>();
    private ArrayList<ColorPicker> colorOptions = new ArrayList<>();

    private boolean isEdit;
    private String selectedActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);

        // Get db connection
        db = AppDatabase.getAppDatabase(getApplicationContext());

        // Get widget references
        titleTextView = findViewById(R.id.createActivityTitle);
        activityNameEditText = findViewById(R.id.createActivityNameEditText);
        addRuleButton = findViewById(R.id.createActivityAddRuleButton);
        saveButton = findViewById(R.id.createActivitySaveButton);
        deleteButton = findViewById(R.id.createActivityDeleteButton);
        backButton = findViewById(R.id.createActivityBackButton);
        ruleListView = findViewById(R.id.CreateActivityListView);

        // Get values from previous screen
        isEdit = getIntent().getBooleanExtra("edit", false);
        selectedActivityId = getIntent().getStringExtra("activityId");

        // Populate Rule menu items
        for(SettingType settingType: SettingType.values()) {
            rulesMenuItems.add(settingType.getValue());
        }


        //put colors into the color picker

        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.activity_maroon), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.activity_green), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.activity_yellow), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.actvity_brown1), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.activity_blue), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.activity_lavender), false));

        colorAdapter = new ColorAdapter(getApplicationContext(), colorOptions);
        GridView colorGrid = findViewById(R.id.createActivityColorGrid);
        colorGrid.setAdapter(colorAdapter);

        // Set Rule ListView Adapter
        rulesSet = new TreeSet<>((ruleAdapterItem, t1) -> ruleAdapterItem.getName().compareTo(t1.getName()));
        ruleAdapter = new RuleAdapter(this, new ArrayList<>(rulesSet), true);
        ruleListView.setAdapter(ruleAdapter);

        // Show menu onClick
        addRuleButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CreateActivityScreen.this, addRuleButton);
            rulesMenuItems.forEach(setting -> {
                if(!setting.equals(SettingType.STEP_COUNT.getValue())) {
                    popup.getMenu().add(setting);
                }
            });
            popup.getMenuInflater().inflate(R.menu.add_rule_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                String settingString = item.getTitle().toString();
                Rule newRule = null;

                if(settingString.equals(SettingType.RINGER.getValue())) {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && !notificationManager.isNotificationPolicyAccessGranted()) {

                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                        startActivity(intent);
                    }
                    newRule = new RingerRule(getApplicationContext(), AudioManager.RINGER_MODE_NORMAL);
                } else if(settingString.equals(SettingType.DRIVING_MODE.getValue())) {
                    newRule = new DrivingModeRule(getApplicationContext(), UiModeManager.DISABLE_CAR_MODE_GO_HOME);
                } else if(settingString.equals(SettingType.NIGHT_MODE.getValue())) {
                    newRule = new NightModeRule(getApplicationContext(), UiModeManager.MODE_NIGHT_NO);
                } else if(settingString.equals(SettingType.STEP_COUNT.getValue())) {
//                    newRule = new StepCounterRule(getApplicationContext());
                }

                if(newRule != null) {
                    rulesSet.clear();
                    rulesSet.addAll(ruleAdapter.getRules());
                    rulesSet.add(new RuleAdapterItem(newRule));
                    ruleAdapter.updateData(new ArrayList<>(rulesSet));
                }

                return true;
            });

            popup.show();
        });

        // Save Activity onClick
        saveButton.setOnClickListener(view -> {
            if(activityNameIsValid()) {
                if(activityColorIsValid()) {
                    if(activityRulesAreValid()) {
                        saveOrUpdate(isEdit);
                        Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                        intent.putExtra("location", "Activity");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Add a Rule", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Select a Color", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Activity Name", Toast.LENGTH_LONG).show();
            }
        });

        // Delete selected activity and associated rules
        deleteButton.setOnClickListener(view -> {
            db.activityDao().deleteActivityById(selectedActivityId);
            Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
            intent.putExtra("location", "Activity");
            startActivity(intent);
        });

        // Abort and redirect onClick
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
            intent.putExtra("location", "Activity");
            startActivity(intent);
        });

        // Populate widgets if isEdit
        setWidgets(isEdit);
    }

    private void setWidgets(boolean isEdit) {
        if (isEdit) {
            Activity currentActivity = new Activity(getApplicationContext(), selectedActivityId);
            titleTextView.setText(R.string.edit_activity_title);
            activityNameEditText.setText(currentActivity.getName());
            currentActivity.getRules().forEach(rule -> rulesSet.add(new RuleAdapterItem(rule)));
            ruleAdapter.updateData(new ArrayList<>(rulesSet));
            for (int i = 0; i < colorOptions.size(); i++) {
                if (currentActivity.getColor() == colorOptions.get(i).getColor()) {
                    colorOptions.get(i).setSelected(true);
                }
            }
        }
        else {
            titleTextView.setText(R.string.create_activity_title);
        }
    }

    private boolean activityNameIsValid() {
        String activityName = activityNameEditText.getText().toString();
        if(!activityName.isEmpty()) {
            List<String> existingActivityNames = new ArrayList<>();
            db.activityDao().findAllActivities().forEach(a -> existingActivityNames.add(a.getName()));

            if(isEdit) {
                String oldActivityName = db.activityDao().findActivityById(selectedActivityId).getName();
                if(activityName.equals(oldActivityName)) {
                    return true;
                }
            }
            return !existingActivityNames.contains(activityName);
        }
        return false;
    }

    private boolean activityColorIsValid() {
        return colorAdapter.getCurrentColor() != null;
    }

    private boolean activityRulesAreValid() {
        return ruleAdapter.getCount() > 0;
    }

    private void saveOrUpdate(boolean isEdit) {
        String activityName = activityNameEditText.getText().toString();
        Integer activityColor = colorAdapter.getCurrentColor();

        if(isEdit) {
            ActivityDb activityDb = db.activityDao().findActivityById(selectedActivityId);
            activityDb.setName(activityName);
            activityDb.setColor(activityColor);
            db.activityDao().update(activityDb);
        } else {
            ActivityDb activityDb = new ActivityDb(activityName, activityColor);
            db.activityDao().insert(activityDb);
            selectedActivityId = activityDb.getId();
        }

        db.ruleDao().deleteRulesForActivity(selectedActivityId);
        for (RuleAdapterItem rule: ruleAdapter.getRules()) {
            String ruleName = rule.getName();
            RuleDb ruleDb = null;

            if(ruleName.equals(SettingType.RINGER.getValue())) {
                ruleDb = new RuleDb(selectedActivityId, SettingType.RINGER, rule.getValue());
            } else if(ruleName.equals(SettingType.DRIVING_MODE.getValue())) {
                ruleDb = new RuleDb(selectedActivityId, SettingType.DRIVING_MODE, rule.getValue());
            } else if(ruleName.equals(SettingType.NIGHT_MODE.getValue())) {
                ruleDb = new RuleDb(selectedActivityId, SettingType.NIGHT_MODE, rule.getValue());
            } else if(ruleName.equals(SettingType.STEP_COUNT.getValue())) {
                ruleDb = new RuleDb(selectedActivityId, SettingType.STEP_COUNT, rule.getValue());
            }

            db.ruleDao().insert(ruleDb);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
        intent.putExtra("location", "Activity");
        startActivity(intent);
    }
}
