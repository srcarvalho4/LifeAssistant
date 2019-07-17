package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateActivityActivity extends AppCompatActivity {

    List<String> rulesMenuItems = new ArrayList<>();

    private EditText activityNameEditText;
    private Button addRuleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity);

        rulesMenuItems.add("Sound");
        rulesMenuItems.add("Location");
        rulesMenuItems.add("Something else");

        activityNameEditText = findViewById(R.id.createActivityNameEditText);
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
}
