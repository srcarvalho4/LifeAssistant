package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

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
import edu.northeastern.lifeassistant.db.models.RuleDb;
import utils.Activity;
import utils.RuleAdapter;
import utils.RuleAdapterItem;

// TODO: ADD ACTIVITY COLOR PICKER
public class CreateActivityScreen extends AppCompatActivity {

    private AppDatabase db;

    ArrayList<String> rulesMenuItems = new ArrayList<>();
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    private EditText activityNameEditText;
    private Button addRuleButton;
    private Button saveButton;
    private Button cancelButton;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        TextView textView = findViewById(R.id.createActivityTitle);
        activityNameEditText = findViewById(R.id.createActivityNameEditText);

        String activityName = getIntent().getStringExtra("name");
        Activity myActivity;


        if (getIntent().getBooleanExtra("edit", false)) {
            myActivity = new Activity(getApplicationContext(), activityName);
            textView.setText("Edit Activity");
            activityNameEditText.setText(myActivity.getName());
            for (int i = 0; i < myActivity.getRules().size(); i++) {
                rules.add(new RuleAdapterItem(myActivity.getRules().get(i)));
                rulesMenuItems.add(rules.get(i).getName());
            }
        } else {
            textView.setText("Create Activity");
        }



        listView = findViewById(R.id.CreateActivityListView);

        RuleAdapter adapter = new RuleAdapter(this, rules);

        listView.setAdapter(adapter);

        addRuleButton = findViewById(R.id.createActivityAddRuleButton);


        addRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CreateActivityScreen.this, addRuleButton);

                for (String rule : rulesMenuItems) {
                    popup.getMenu().add(rule);
                }

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

        saveButton = findViewById(R.id.createActivitySaveButton);
        cancelButton = findViewById(R.id.createActivityCancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activityName = activityNameEditText.getText().toString();
                ActivityDb activityDb = new ActivityDb(activityName, Color.BLUE);
                db.activityDao().insert(activityDb);

                for (RuleAdapterItem rule: rules) {
//                    db.ruleDao().insert(new RuleDb(activityDb.getId(), rule.get));
                }
            }
        });
    }
}