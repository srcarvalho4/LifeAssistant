package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import utils.DisplayRule;
import utils.RingerRule;
import utils.RuleAdapter;
import utils.RuleAdapterItem;

public class CreateActivityActivity extends AppCompatActivity {

    ArrayList<String> rulesMenuItems = new ArrayList<>();
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();

    private EditText activityNameEditText;
    private Button addRuleButton;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);

        rulesMenuItems.add("Driving Mode");
        rulesMenuItems.add("Airplane Mode");

        populateList();


        TextView textView = findViewById(R.id.createActivityTitle);
        activityNameEditText = findViewById(R.id.createActivityNameEditText);

        if (getIntent().getBooleanExtra("edit", false)) {
            textView.setText("Edit Activity");
            activityNameEditText.setText(getIntent().getStringExtra("name"));
        }
        else {
            textView.setText("Create Activity");
        }

        listView = findViewById(R.id.CreateActivityListView);

        RuleAdapter adapter = new RuleAdapter(this, rules);

        listView.setAdapter(adapter);

        addRuleButton = findViewById(R.id.createActivityAddRuleButton);


        addRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CreateActivityActivity.this, addRuleButton);

                for(String rule : rulesMenuItems) {
                    popup.getMenu().add(rule);
                }

                popup.getMenuInflater().inflate(R.menu.add_rule_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(CreateActivityActivity.this,
                                item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    private void populateList() {
        rules.add(new RuleAdapterItem(new DisplayRule("Do Not Disturb Mode")));
        rules.add(new RuleAdapterItem(new DisplayRule("Sound")));
        rules.add(new RuleAdapterItem(new DisplayRule("Location")));
    }
}
