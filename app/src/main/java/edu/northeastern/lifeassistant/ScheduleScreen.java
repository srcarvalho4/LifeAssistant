package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import util.EventAdapter;
import util.EventAdapterItem;

public class ScheduleScreen extends AppCompatActivity {

    ListView listView;

    ArrayList<EventAdapterItem> events = new ArrayList<>();

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
        events.add(new EventAdapterItem("5 Mile Jog", "8:00am", "9:00am", Color.rgb(100, 240, 100), 0b01000001));
        events.add(new EventAdapterItem("Mobile App Development", "11:40am", "1:20pm", Color.rgb(240, 100, 100), 0b00011110));
        events.add(new EventAdapterItem("Study Session", "6:30pm", "9:00pm", Color.rgb(100, 100, 240), 0b01111111));
        events.add(new EventAdapterItem("Group Meeting", "3:00pm", "5:00pm", Color.rgb(100, 100, 240), 0b00001010));
    }
}
