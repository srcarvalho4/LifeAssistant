package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import utils.Activity;
import utils.ActivityAdapter;
import utils.EventAdapter;
import utils.Rule;
import utils.ScheduleEvent;

public class ScheduleScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;
    boolean filterShowing = false;
    LinearLayout filterWindow;

    ArrayList<ScheduleEvent> events = new ArrayList<>();
    ArrayList<Activity> allActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_screen);

        listView = findViewById(R.id.scheduleListView);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        final List<ScheduleEventDb> scheduleEventDb = db.scheduleEventDao().findAllScheduleEvents();
        final List<ActivityDb> activityDb = db.activityDao().findAllActivities();

        for (int i = 0; i < scheduleEventDb.size(); i++) {
            events.add(new ScheduleEvent(getApplicationContext(), scheduleEventDb.get(i).getId()));
        }

        allActivities.add(new Activity(Color.WHITE, "All", new ArrayList<>()));
        for (int i = 0; i < activityDb.size(); i++) {
            allActivities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }

        EventAdapter adapter = new EventAdapter(this, events);

        listView.setAdapter(adapter);

        Button addButton = findViewById(R.id.scheduleActivityButtonAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleScreen.this, CreateEventScreen.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        Button filterButton = findViewById(R.id.scheduleActivityButtonFilter);
        TextView filterName = findViewById(R.id.scheduleActivityFilterIndicator);
        filterWindow = findViewById(R.id.scheduleFilterView);

        ActivityAdapter activityAdapter = new ActivityAdapter(this, allActivities);
        ListView filterView = findViewById(R.id.scheduleListFilterView);
        filterView.setAdapter(activityAdapter);

        filterName.setText("All");
        filterName.setTextColor(Color.BLACK);

        filterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<ScheduleEvent> newEvents = new ArrayList<>();

                if (i == 0) {
                    newEvents = events;
                    filterName.setText("All");
                    filterName.setTextColor(Color.BLACK);
                } else {
                    Activity filterActivity = new Activity(getApplicationContext(), activityDb.get(i - 1).getId());
                    for (int j = 0; j < events.size(); j++) {
                        if (events.get(j).getActivityType().getName().equals(filterActivity.getName())) {
                            newEvents.add(events.get(j));
                        }
                    }
                    filterName.setText(filterActivity.getName());
                    int color = filterActivity.getColor();
                    int darkColorR = (int) (((color >> 16) & 0xff) * 3 / 4);
                    int darkColorG = (int) (((color >>  8) & 0xff) * 3 / 4);
                    int darkColorB = (int) (((color) & 0xff) * 3 / 4);
                    int darkColor = Color.rgb(darkColorR, darkColorG, darkColorB);
                    filterName.setTextColor(darkColor);
                }
                filterWindow.setVisibility(View.GONE);
                filterShowing = false;
                adapter.updateData(newEvents);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterWindow.setVisibility(View.VISIBLE);
                filterShowing = true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ScheduleScreen.this, CreateEventScreen.class);
                intent.putExtra("eventId", scheduleEventDb.get(i).getId());
                intent.putExtra("edit", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (filterShowing) {
            filterWindow.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
//        this.finish();
    }
}