package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.dao.ActivityDao;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import utils.Activity;
import utils.ActivityAdapter;
import utils.DrivingModeRule;
import utils.NightModeRule;
import utils.RingerRule;
import utils.Rule;

public class ActivityScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen);

        listView = findViewById(R.id.activityListView);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        List<ActivityDb> activityDb = new ArrayList<>();

        activityDb = db.activityDao().findAllActivities();

        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        Button button = findViewById(R.id.activityListButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityScreen.this, CreateActivityActivity.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityScreen.this, CreateActivityActivity.class);

                intent.putExtra("name", activities.get(i).getTypeName());
                intent.putExtra("color", activities.get(i).getColor());
                intent.putExtra("edit", true);
                startActivity(intent);
            }
        });
    }
}
