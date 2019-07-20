package utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.lifeassistant.R;

public class ActivityAdapter extends BaseAdapter {

    Context context;
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

        if (view == null) {
            view = View.inflate(context, R.layout.list_activity_item, null);

            viewHolder = new ActivityViewHolder();

            viewHolder.textView = view.findViewById(R.id.activityItemText);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ActivityViewHolder) view.getTag();
        }


        viewHolder.textView.setText(activities.get(i).getTypeName());
        viewHolder.textView.setBackgroundColor(activities.get(i).getColor());


        return view;
    }
}

class ActivityViewHolder {
    TextView textView;
}