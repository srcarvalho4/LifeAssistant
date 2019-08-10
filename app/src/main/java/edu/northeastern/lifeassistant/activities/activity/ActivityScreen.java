package edu.northeastern.lifeassistant.activities.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.activities.SplashScreen;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.utils.items.Activity;
import edu.northeastern.lifeassistant.utils.adapters.ActivityAdapter;

public class ActivityScreen extends AppCompatActivity {

    ListView listView;

    AppDatabase db;

    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen);

        listView = findViewById(R.id.activityListView);

        //load database reference
        db = AppDatabase.getAppDatabase(getApplicationContext());

        //load activity table
        List<ActivityDb> activityDb = db.activityDao().findAllActivities();

        ActivityDb activityDb1 = db.activityDao().findActivityByName("Class");

        //Log.d("MyColor", activityDb1.getColor().toString());

        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        ImageButton button = findViewById(R.id.activityListButton);

        //navigation to the activity edit screen
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityScreen.this, CreateActivityScreen.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityScreen.this, CreateActivityScreen.class);

                intent.putExtra("activityId", activityDb.get(i).getId());
                intent.putExtra("edit", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityScreen.this, SplashScreen.class);
        startActivity(intent);
    }
}
