package edu.northeastern.lifeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.SpontaneousEvent;

public class ViewStepsHistoryActivity extends AppCompatActivity {
    String myString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_steps_history);

        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        List<SpontaneousEvent> check = db.spontaneousEventDao().findAllSpontaneousEvents();

        View linearLayout =  findViewById(R.id.viewHistory);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.info);

        //TextView valueTV = new TextView(this);
        //valueTV.setText("hallo hallo");
        //valueTV.setId(5);
        //valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        //((LinearLayout) linearLayout).addView(valueTV);

        if (check == null)
        {
            Log.d("Steps", "The check object is null");
        }
        else {
            for (int i=0; i<check.size(); i++)
            {
                if (check.get(i).getEndTime() != null) {
                    myString = myString + check.get(i).getStartTime().getTime().toString() + "\n";
                    myString = myString + check.get(i).getEndTime().getTime().toString() + "\n";
                    myString = myString + check.get(i).getFinalValue() + "\n";
                    TextView entry = new TextView(this);
                    entry.setText(myString);
                    //String textViewId = "textView" + Integer.toString(i);
                    entry.setId(i);
                    entry.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(entry);

                }

            }
        }


    }
}
