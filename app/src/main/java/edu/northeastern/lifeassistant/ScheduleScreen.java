package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import utils.Activity;
import utils.EventAdapter;
import utils.Rule;
import utils.ScheduleEvent;

public class ScheduleScreen extends AppCompatActivity {

    ListView listView;


    ArrayList<ScheduleEvent> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_screen);

        listView = findViewById(R.id.scheduleListView);

        populateList();

        EventAdapter adapter = new EventAdapter(this, events);

        listView.setAdapter(adapter);

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
        /*
        activities.add(new Activity(Color.rgb(100,240, 100), "Running", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(240,100, 100), "Class", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(100,100, 240), "Studying", new ArrayList<Rule>()));
         */
        //TODO
        Activity runningActivity = new Activity(Color.rgb(100,240, 100), "Running", new ArrayList<Rule>());
        Activity classActivity = new Activity(Color.rgb(240,100, 100), "Class", new ArrayList<Rule>());
        Activity studyActivity = new Activity(Color.rgb(100,100, 240), "Studying", new ArrayList<Rule>());
    }
}
