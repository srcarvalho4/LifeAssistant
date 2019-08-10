package edu.northeastern.lifeassistant.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.utils.items.Activity;

public class ActivityAdapter extends BaseAdapter {

    final Context context;
    ArrayList<Activity> activities;

    public ActivityAdapter(Context context, ArrayList<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Object getItem(int i) {
        return activities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ActivityViewHolder viewHolder;

        //viewholder setup
        if (view == null) {
            view = View.inflate(context, R.layout.list_activity_item, null);

            viewHolder = new ActivityViewHolder();

            viewHolder.textView = view.findViewById(R.id.activityItemText);
            viewHolder.background = view.findViewById(R.id.activityItemWindow);

//            viewHolder.textView.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (MotionEvent.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                        AppDatabase.getAppDatabase(context);
//                    }
//                    return false;
//                }
//            });

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ActivityViewHolder) view.getTag();
        }


        //set value based on data
        viewHolder.textView.setText(activities.get(i).getName());
        viewHolder.textView.setBackgroundColor(activities.get(i).getColor());


        return view;
    }

    class ActivityViewHolder {
        TextView textView;
        CardView background;

    }
}