package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import utils.Activity;
import utils.ActivityAdapter;
import utils.Rule;

public class SpontaneousScreen extends AppCompatActivity {

    ListView listView;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spontaneous_screen);

        listView = findViewById(R.id.SpontaneousScreenList);

        populateList();

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SpontaneousScreen.this, SpontaneousActive.class);

                intent.putExtra("name", activities.get(i).getTypeName());
                intent.putExtra("color", activities.get(i).getColor());
                intent.putExtra("location", "Spontaneous");
                startActivity(intent);
            }
        });
    }

    private void populateList() {
        activities.add(new Activity(Color.rgb(100,240, 100), "Running", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(240,100, 100), "Class", new ArrayList<Rule>()));
        activities.add(new Activity(Color.rgb(100,100, 240), "Studying", new ArrayList<Rule>()));
    }
}
