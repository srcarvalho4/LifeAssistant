package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import utils.Activity;
import utils.ActivityAdapter;
import utils.Rule;

public class ActivityScreen extends AppCompatActivity {

    ListView listView;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen);

        listView = findViewById(R.id.activityListView);

        populateList();

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        Button button = findViewById(R.id.activityListButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityScreen.this, CreateActivityActivity.class);
                startActivity(intent);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(ActivityScreen.this, CharacterActivity.class);
//
//                intent.putExtra("character", favoriteCharacters.get(i).getCharacterName());
//                startActivity(intent);
//            }
//        });
    }

    private void populateList() {
        activities.add(new Activity(Color.rgb(100,240, 100), "Running", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(240,100, 100), "Class", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(100,100, 240), "Studying", new ArrayList<Rule>()));
    }
}
