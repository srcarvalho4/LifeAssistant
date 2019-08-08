package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import utils.Activity;
import utils.ActivityAdapter;

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

        List<ActivityDb> activityDb = db.activityDao().findAllActivities();

        for (int i = 0; i < activityDb.size(); i++) {
            activities.add(new Activity(getApplicationContext(), activityDb.get(i).getId()));
        }

        ActivityAdapter adapter = new ActivityAdapter(this, activities);

        listView.setAdapter(adapter);

        ImageButton button = findViewById(R.id.activityListButton);

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
        this.finish();
    }
}
