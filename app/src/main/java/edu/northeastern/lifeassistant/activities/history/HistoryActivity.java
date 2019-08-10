package edu.northeastern.lifeassistant.activities.history;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;
import edu.northeastern.lifeassistant.utils.adapters.HistoryAdapter;
import edu.northeastern.lifeassistant.utils.items.HistoryAdapterInfoItem;

import static android.media.CamcorderProfile.get;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HistoryAdapterInfoItem> myHistoryItems = new ArrayList<HistoryAdapterInfoItem>();
    int imageSelection;
    String finalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        listView = findViewById(R.id.myHistoryListView);
        imageSelection = R.drawable.running;

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
        //Pulling spontaneous activity data from database
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<SpontaneousEventDb> check = db.spontaneousEventDao().findAllSpontaneousEventsInDescendingOrder();


        if (check == null)
        {
            Log.d("Steps", "No Data Found");
        }
        else {
            for (int i = 0; i < check.size(); i++) {
                if (check.get(i).getEndTime() != null) {

                    //Setting activity specific logos
                    AppDatabase db1 = AppDatabase.getAppDatabase(getApplicationContext());
                    ActivityDb check2 = db1.activityDao().findActivityById(check.get(i).getActivityId());

                    Log.d("Details", "Activity Name: " + check2.getName());
                    Log.d("Details", "StepCount: " + check.get(i).getFinalValue());

                    if (check2.getName().equalsIgnoreCase("class")) {
                        imageSelection = R.drawable.bookfav;
                        finalValue = "";
                    }
                    else if (check2.getName().equalsIgnoreCase("running")){
                        imageSelection = R.drawable.icon_runningman;
                        finalValue = check.get(i).getFinalValue();
                    }
                    else {
                        imageSelection = R.drawable.icon_custom_activity;
                        finalValue = "";
                    }

                    String[] expected1 = check.get(i).getStartTime().getTime().toString().split(" ");
                    String myTimeString1 = expected1[1] + " " + expected1[2] + "\n" + expected1[3] + expected1[4];

                    String[] expected2 = check.get(i).getEndTime().getTime().toString().split(" ");
                    String myTimeString2 = expected2[1] + " " + expected2[2] + "\n" + expected2[3] + expected2[4];

                    //populating the History listview - myHistoryItems arraylist
                    myHistoryItems.add(new HistoryAdapterInfoItem(imageSelection, check2.getName(),
                            finalValue, myTimeString1, myTimeString2, check.get(i).getActivityId(), check.get(i).getId()));



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
