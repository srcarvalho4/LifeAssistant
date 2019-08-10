package edu.northeastern.lifeassistant.activities.event;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.SplashScreen;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import edu.northeastern.lifeassistant.utils.items.Activity;
import edu.northeastern.lifeassistant.utils.adapters.ActivityAdapter;
import edu.northeastern.lifeassistant.utils.adapters.EventAdapter;
import edu.northeastern.lifeassistant.utils.items.ScheduleEvent;

public class ScheduleScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;
    boolean filterShowing = false;
    LinearLayout filterWindow;
    ImageView filterWindowCover;

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

        allActivities.add(new Activity(Color.BLACK, "All", new ArrayList<>()));
        for (int i = 0; i < activityDb.size(); i++) {
            allActivities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }

        EventAdapter adapter = new EventAdapter(this, events);

        listView.setAdapter(adapter);

        ImageButton addButton = findViewById(R.id.scheduleActivityButtonAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleScreen.this, CreateEventScreen.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        ImageButton filterButton = findViewById(R.id.scheduleActivityButtonFilter);
        TextView filterName = findViewById(R.id.scheduleActivityFilterIndicator);
        filterWindow = findViewById(R.id.scheduleFilterView);
        filterWindowCover = findViewById(R.id.scheduleFilterViewCover);

        ActivityAdapter activityAdapter = new ActivityAdapter(this, allActivities);
        ListView filterView = findViewById(R.id.scheduleListFilterView);
        filterView.setAdapter(activityAdapter);

        filterName.setVisibility(View.GONE);

        filterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //update the filter based on the chosen value
                ArrayList<ScheduleEvent> newEvents = new ArrayList<>();

                if (i == 0) {
                    //if the value is zero, the "ALL" option was picked
                    newEvents = events;
                    filterName.setVisibility(View.GONE);
                } else {
                    //otherwise, the real value is i-1, as index 0 has the "ALL" option
                    Activity filterActivity = new Activity(getApplicationContext(), activityDb.get(i - 1).getId());
                    for (int j = 0; j < events.size(); j++) {
                        if (events.get(j).getActivityType().getName().equals(filterActivity.getName())) {
                            newEvents.add(events.get(j));
                        }
                    }
                    filterName.setText(filterActivity.getName());
                    filterName.setVisibility(View.VISIBLE);
//                    int color = filterActivity.getColor();
//                    int darkColorR = (int) (((color >> 16) & 0xff) * 3 / 4);
//                    int darkColorG = (int) (((color >>  8) & 0xff) * 3 / 4);
//                    int darkColorB = (int) (((color) & 0xff) * 3 / 4);
//                    int darkColor = Color.rgb(darkColorR, darkColorG, darkColorB);
//                    filterName.setTextColor(darkColor);
                }
                filterWindow.setVisibility(View.GONE);
                filterWindowCover.setVisibility(View.GONE);
                filterShowing = false;
                adapter.updateData(newEvents);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterWindow.setVisibility(View.VISIBLE);
                filterWindowCover.setVisibility(View.VISIBLE);
                filterShowing = true;
            }
        });

        filterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterWindow.setVisibility(View.VISIBLE);
                filterWindowCover.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ScheduleScreen.this, SplashScreen.class);
            startActivity(intent);
        }
    }
}