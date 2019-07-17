package util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.northeastern.lifeassistant.R;

public class EventAdapter extends BaseAdapter {

    Context context;
    ArrayList<EventAdapterItem> events;

    public EventAdapter(Context context, ArrayList<EventAdapterItem> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        EventViewHolder viewHolder;

        if (view == null) {
            view = View.inflate(context, R.layout.list_event_item, null);

            viewHolder = new EventViewHolder();

            viewHolder.main = view.findViewById(R.id.eventItemGrid);
            viewHolder.name = view.findViewById(R.id.eventItemName);
            viewHolder.startTime = view.findViewById(R.id.eventItemStartTime);
            viewHolder.endTime = view.findViewById(R.id.eventItemEndTime);
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemSunday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemMonday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemTuesday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemWednesday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemThursday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemFriday));
            viewHolder.days.add((TextView) view.findViewById(R.id.eventItemSaturday));

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (EventViewHolder) view.getTag();
        }


        viewHolder.name.setText(events.get(i).getName());
        viewHolder.startTime.setText("From: " + events.get(i).getStartTime());
        viewHolder.endTime.setText("To: " + events.get(i).getEndTime());
        viewHolder.main.setBackgroundColor(events.get(i).getColor());

        int data = events.get(i).getDayData();
        for (int j = 0; j <= 6; j++) {
            if (data % 2 == 0){
                viewHolder.days.get(j).setVisibility(View.INVISIBLE);
            }
            else {
                viewHolder.days.get(j).setVisibility(View.VISIBLE);
            }
            data = data >> 1;
        }


        return view;
    }
}

class EventViewHolder {
    GridLayout main;
    TextView name;
    TextView startTime;
    TextView endTime;
    ArrayList<TextView> days = new ArrayList<>();
}