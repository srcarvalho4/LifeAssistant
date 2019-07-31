package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.types.SettingType;
import utils.Activity;
import utils.RuleAdapter;
import utils.RuleAdapterItem;

public class CreateActivityScreen extends AppCompatActivity {

    private AppDatabase db;

    private ArrayList<String> rulesMenuItems = new ArrayList<>();
    private ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    private TextView titleTextView;
    private EditText activityNameEditText;
    private Button addRuleButton;
    private Button saveButton;
    private Button cancelButton;

    private ListView ruleListView;

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
        cancelButton = findViewById(R.id.createActivityCancelButton);
        ruleListView = findViewById(R.id.CreateActivityListView);

        // Get values from previous screen
        boolean isEdit = getIntent().getBooleanExtra("edit", false);
        String activityId = getIntent().getStringExtra("activityId");

        // Populate Rule menu items
        for(SettingType settingType: SettingType.values()) {
            rulesMenuItems.add(settingType.getValue());
        }

        // Populate widgets if isEdit
        if (isEdit) {
            Activity currentActivity = new Activity(getApplicationContext(), activityId);
            titleTextView.setText(R.string.edit_activity_title);
            activityNameEditText.setText(currentActivity.getName());
            currentActivity.getRules().forEach(rule -> rules.add(new RuleAdapterItem(rule)));
        } else {
            titleTextView.setText(R.string.create_activity_title);
        }

        // Set Rule ListView Adapter
        ruleListView.setAdapter(new RuleAdapter(this, rules));

        // Show menu onClick
        addRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CreateActivityScreen.this, addRuleButton);
                rulesMenuItems.forEach(rule -> popup.getMenu().add(rule));
                popup.getMenuInflater().inflate(R.menu.add_rule_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
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
//                String activityName = activityNameEditText.getText().toString();
//                ActivityDb activityDb = new ActivityDb(activityName, Color.BLUE);
//                db.activityDao().insert(activityDb);
//
//                for (RuleAdapterItem rule: rules) {
//                    db.ruleDao().insert(new RuleDb(activityDb.getId(), rule.get));
//                }
            }
        });

        // Abort and redirect onClick
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityScreen.class);
                startActivity(intent);
            }
        });
    }
}
