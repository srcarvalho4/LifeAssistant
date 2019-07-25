package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;
import utils.Activity;
import utils.EventAdapter;
import utils.Rule;
import utils.ScheduleEvent;

public class ScheduleScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;

    ArrayList<ScheduleEvent> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_screen);

        listView = findViewById(R.id.scheduleListView);

        List<ScheduleEventDb> scheduleEventDb = new ArrayList<>();

        db = AppDatabase.getAppDatabase(getApplicationContext());

        scheduleEventDb = db.scheduleEventDao().findAllScheduleEvents();

        for (int i = 0; i < scheduleEventDb.size(); i++) {
            events.add(new ScheduleEvent(getApplicationContext(), scheduleEventDb.get(i).getId()));
        }

        EventAdapter adapter = new EventAdapter(this, events);

        listView.setAdapter(adapter);

        Button button = findViewById(R.id.scheduleActivityButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleScreen.this, CreateEventActivity.class);
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

//    private void populateList() {
//        //TODO
//        Activity runningActivity = new Activity(Color.rgb(140,240, 120), "Running", new ArrayList<Rule>());
//        Activity classActivity = new Activity(Color.rgb(220,120, 120), "Class", new ArrayList<Rule>());
//        Activity studyActivity = new Activity(Color.rgb(140,140, 240), "Studying", new ArrayList<Rule>());
//
//        Calendar startTime1 = Calendar.getInstance();
//        startTime1.set(Calendar.AM_PM, Calendar.AM);
//        startTime1.set(Calendar.MINUTE, 0);
//        startTime1.set(Calendar.HOUR, 8);
//
//        Calendar endTime1 = Calendar.getInstance();
//        endTime1.set(Calendar.AM_PM, Calendar.AM);
//        endTime1.set(Calendar.MINUTE, 0);
//        endTime1.set(Calendar.HOUR, 9);
//
//
//        Calendar startTime2 = Calendar.getInstance();
//        startTime2.set(Calendar.AM_PM, Calendar.AM);
//        startTime2.set(Calendar.MINUTE, 40);
//        startTime2.set(Calendar.HOUR, 11);
//
//        Calendar endTime2 = Calendar.getInstance();
//        endTime2.set(Calendar.AM_PM, Calendar.PM);
//        endTime2.set(Calendar.MINUTE, 20);
//        endTime2.set(Calendar.HOUR, 1);
//
//
//        Calendar startTime3 = Calendar.getInstance();
//        startTime3.set(Calendar.AM_PM, Calendar.PM);
//        startTime3.set(Calendar.MINUTE, 0);
//        startTime3.set(Calendar.HOUR, 6);
//
//        Calendar endTime3 = Calendar.getInstance();
//        endTime3.set(Calendar.AM_PM, Calendar.PM);
//        endTime3.set(Calendar.MINUTE, 0);
//        endTime3.set(Calendar.HOUR, 9);
//
//
//        Calendar startTime4 = Calendar.getInstance();
//        startTime4.set(Calendar.AM_PM, Calendar.PM);
//        startTime4.set(Calendar.MINUTE, 0);
//        startTime4.set(Calendar.HOUR, 3);
//
//        Calendar endTime4 = Calendar.getInstance();
//        endTime4.set(Calendar.AM_PM, Calendar.PM);
//        endTime4.set(Calendar.MINUTE, 0);
//        endTime4.set(Calendar.HOUR, 5);
//
//        ArrayList<Integer> days1 = new ArrayList<>();
//        days1.add(Calendar.SUNDAY);
//        days1.add(Calendar.MONDAY);
//
//        ArrayList<Integer> days2 = new ArrayList<>();
//        days2.add(Calendar.MONDAY);
//        days2.add(Calendar.TUESDAY);
//        days2.add(Calendar.WEDNESDAY);
//        days2.add(Calendar.THURSDAY);
//
//        ArrayList<Integer> days3 = new ArrayList<>();
//        days3.add(Calendar.MONDAY);
//        days3.add(Calendar.TUESDAY);
//        days3.add(Calendar.WEDNESDAY);
//        days3.add(Calendar.THURSDAY);
//        days3.add(Calendar.FRIDAY);
//
//        ArrayList<Integer> days4 = new ArrayList<>();
//        days4.add(Calendar.MONDAY);
//        days4.add(Calendar.WEDNESDAY);
//
//        events.add(new ScheduleEvent(runningActivity, "Morning Jog", startTime1, endTime1, days1));
//        events.add(new ScheduleEvent(classActivity, "Mobile App Development", startTime2, endTime2, days2));
//        events.add(new ScheduleEvent(studyActivity, "Private Study", startTime3, endTime3, days3));
//        events.add(new ScheduleEvent(studyActivity, "Group Work", startTime4, endTime4, days4));
//    }
}
