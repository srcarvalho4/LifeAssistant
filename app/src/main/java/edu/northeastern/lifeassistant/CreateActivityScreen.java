package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import android.app.UiModeManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.types.SettingType;
import utils.Activity;
import utils.ColorAdapter;
import utils.ColorPicker;
import utils.DrivingModeRule;
import utils.NightModeRule;
import utils.RingerRule;
import utils.Rule;
import utils.RuleAdapter;
import utils.RuleAdapterItem;
import utils.StepCounterRule;

public class CreateActivityScreen extends AppCompatActivity {

    private AppDatabase db;

    private ArrayList<String> rulesMenuItems = new ArrayList<>();
    private ArrayList<RuleAdapterItem> rules = new ArrayList<>();
    private ArrayList<ColorPicker> colorOptions = new ArrayList<>();

    private TextView titleTextView;
    private EditText activityNameEditText;
    private Button addRuleButton;
    private Button saveButton;
    private Button deleteButton;

    private ListView ruleListView;
    private ColorAdapter colorAdapter;

    private Button redColor;
    private Button yellowColor;
    private Button greenColor;
    private Button blueColor;

    private boolean isEdit;
    private String selectedActivityId;

    private RuleAdapter ruleAdapter;

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
        ruleListView = findViewById(R.id.CreateActivityListView);

        // Get values from previous screen
        isEdit = getIntent().getBooleanExtra("edit", false);
        selectedActivityId = getIntent().getStringExtra("activityId");

        // Populate Rule menu items
        for(SettingType settingType: SettingType.values()) {
            rulesMenuItems.add(settingType.getValue());
        }

        //put colors into the color picker

        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.red), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.orange), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.yellow), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.green), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.blue), false));
        colorOptions.add(new ColorPicker(ContextCompat.getColor(getApplicationContext(), R.color.purple), false));

        colorAdapter = new ColorAdapter(getApplicationContext(), colorOptions);

        GridView colorGrid = findViewById(R.id.createActivityColorGrid);


        // Populate widgets if isEdit
        if (isEdit) {
            Activity currentActivity = new Activity(getApplicationContext(), selectedActivityId);
            titleTextView.setText(R.string.edit_activity_title);
            activityNameEditText.setText(currentActivity.getName());
            currentActivity.getRules().forEach(rule -> rules.add(new RuleAdapterItem(rule)));
            for (int i = 0; i < colorOptions.size(); i++) {
                if (currentActivity.getColor() == colorOptions.get(i).getColor()) {
                    colorOptions.get(i).setSelected(true);
                }
            }
        }
        else {
            titleTextView.setText(R.string.create_activity_title);
        }



        colorGrid.setAdapter(colorAdapter);



        // Set Rule ListView Adapter
        ruleAdapter = new RuleAdapter(this, rules);
        ruleListView.setAdapter(ruleAdapter);

        // Show menu onClick
        addRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CreateActivityScreen.this, addRuleButton);
                rulesMenuItems.forEach(rule -> popup.getMenu().add(rule));
                popup.getMenuInflater().inflate(R.menu.add_rule_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String settingString = item.getTitle().toString();
                        Rule newRule = null;

                        if(settingString.equals(SettingType.RINGER.getValue())) {
                            newRule = new RingerRule(getApplicationContext(), AudioManager.RINGER_MODE_NORMAL);
                        } else if(settingString.equals(SettingType.DRIVING_MODE.getValue())) {
                            newRule = new DrivingModeRule(getApplicationContext(), UiModeManager.DISABLE_CAR_MODE_GO_HOME);
                        } else if(settingString.equals(SettingType.NIGHT_MODE.getValue())) {
                            newRule = new NightModeRule(getApplicationContext(), UiModeManager.MODE_NIGHT_NO);
                        } else if(settingString.equals(SettingType.STEP_COUNT.getValue())) {

                        }

                        rules.add(new RuleAdapterItem(newRule));
                        ruleAdapter.notifyDataSetChanged();

                        Toast.makeText(CreateActivityScreen.this,
                                item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
            }
        });

        // Save Activity onClick
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDb activityDb;
                String activityName = activityNameEditText.getText().toString();
                Integer activityColor = colorAdapter.getCurrentColor();

                if(isEdit) {
                    activityDb = db.activityDao().findActivityById(selectedActivityId);
                    activityDb.setName(activityName);
                    activityDb.setColor(activityColor);
                    db.activityDao().update(activityDb);
                } else {
                    activityDb = new ActivityDb(activityName, activityColor);
                    db.activityDao().insert(activityDb);
                }

                for (RuleDb ruledb: db.ruleDao().findRulesForActivity(selectedActivityId)) {
                    db.ruleDao().delete(ruledb);
                }
                for (RuleAdapterItem rule: rules) {
                    String ruleName = rule.getName();
                    RuleDb ruleDb = null;

                    if(ruleName.equals(SettingType.RINGER.getValue())) {
                        ruleDb = new RuleDb(activityDb.getId(), SettingType.RINGER, rule.getValue());
                    } else if(ruleName.equals(SettingType.DRIVING_MODE.getValue())) {
                        ruleDb = new RuleDb(activityDb.getId(), SettingType.DRIVING_MODE, rule.getValue());
                    } else if(ruleName.equals(SettingType.NIGHT_MODE.getValue())) {
                        ruleDb = new RuleDb(activityDb.getId(), SettingType.NIGHT_MODE, rule.getValue());
                    } else if(ruleName.equals(SettingType.STEP_COUNT.getValue())) {

                    }

                    db.ruleDao().insert(ruleDb);
                }

                Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                intent.putExtra("location", "Schedule");
                startActivity(intent);
            }
        });

        // Delete selected activity and associated rules
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.activityDao().deleteActivityById(selectedActivityId);
                Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                intent.putExtra("location", "Activity");
                startActivity(intent);
            }
        });


        ImageButton backButton = findViewById(R.id.createActivityBackButton);
        // Abort and redirect onClick
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                intent.putExtra("location", "Activity");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
        intent.putExtra("location", "Activity");
        startActivity(intent);
    }
}
