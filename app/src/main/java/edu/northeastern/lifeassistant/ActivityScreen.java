package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import util.ActivityAdapter;
import util.ActivityAdapterItem;

public class ActivityScreen extends AppCompatActivity {

    ListView listView;

    ArrayList<ActivityAdapterItem> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen);

        listView = findViewById(R.id.activityListView);

        populateList();

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

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
        activities.add(new ActivityAdapterItem("Running", Color.rgb(100,240, 100)));
        activities.add(new ActivityAdapterItem("Class", Color.rgb(240,100, 100)));
        activities.add(new ActivityAdapterItem("Studying", Color.rgb(100,100, 240)));
    }
}
