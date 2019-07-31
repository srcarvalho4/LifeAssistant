package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;
import utils.HistoryAdapter;
import utils.HistoryAdapterInfoItem;

import static android.media.CamcorderProfile.get;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HistoryAdapterInfoItem> myHistoryItems = new ArrayList<HistoryAdapterInfoItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        listView = findViewById(R.id.myHistoryListView);

        populateList();

        HistoryAdapter myAdapter = new HistoryAdapter(this, myHistoryItems);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this, HistoryInfoActivity.class);
                intent.putExtra("ActivityName", myHistoryItems.get(i).getActivityName());
            }
        });



    }

    private void populateList() {

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<SpontaneousEventDb> check = db.spontaneousEventDao().findAllSpontaneousEvents();


        if (check == null)
        {
            Log.d("Steps", "No Data Found");
        }
        else {
            for (int i = 0; i < check.size(); i++) {
                if (check.get(i).getEndTime() != null) {


                    AppDatabase db1 = AppDatabase.getAppDatabase(getApplicationContext());
                    ActivityDb check2 = db1.activityDao().findActivityById(check.get(i).getActivityId());

                    Log.d("Details", "Activity Name: " + check2.getName());
                    Log.d("Details", "StepCount: " + check.get(i).getFinalValue());



                    myHistoryItems.add(new HistoryAdapterInfoItem(R.drawable.icon_runningman, check2.getName(),
                            check.get(i).getFinalValue(), check.get(i).getStartTime().getTime().toString(),
                            check.get(i).getEndTime().getTime().toString(), check.get(i).getActivityId(), check.get(i).getId()));



                    /*
                    myString = myString + check.get(i).getId() + "\n";
                    Log.d("StepHistory", Integer.toString(i));
                    Log.d("StepHistory", check.get(i).getId());
                    myString = myString + check.get(i).getStartTime().getTime().toString() + "\n";
                    myString = myString + check.get(i).getStartValue() + "\n";
                    myString = myString + check.get(i).getEndTime().getTime().toString() + "\n";
                    myString = myString + check.get(i).getEndValue() + "\n";
                    myString = myString + check.get(i).getFinalValue() + "\n";
                    TextView entry = new TextView(this);
                    entry.setText(myString);
                    //String textViewId = "textView" + Integer.toString(i);
                    myString = "";
                    */

                }
            }

        }



            }


}
