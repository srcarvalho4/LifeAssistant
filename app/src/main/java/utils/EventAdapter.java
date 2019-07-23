package utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import edu.northeastern.lifeassistant.R;

public class EventAdapter extends BaseAdapter {

    Context context;
    ArrayList<ScheduleEvent> events;

    public EventAdapter(Context context, ArrayList<ScheduleEvent> events) {
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

            viewHolder.background = view.findViewById(R.id.EventItemGridLayout);
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


        Calendar start = events.get(i).getStartTime();
        Calendar end = events.get(i).getEndTime();

        String startText = "" + start.get(Calendar.HOUR) + ":";

        if (start.get(Calendar.MINUTE) < 10) {
            startText += "0";
        }
        startText += start.get(Calendar.MINUTE);
        if (start.get(Calendar.AM_PM) == 0) {
            startText += "am";
        }
        else {
            startText += "pm";
        }


        String endText = "" + end.get(Calendar.HOUR) + ":";

        if (end.get(Calendar.MINUTE) < 10) {
            endText += "0";
        }
        endText += end.get(Calendar.MINUTE);
        if (end.get(Calendar.AM_PM) == 0) {
            endText += "am";
        }
        else {
            endText += "pm";
        }


        viewHolder.name.setText(events.get(i).getName());
        viewHolder.startTime.setText(startText);
        viewHolder.endTime.setText(endText);
        viewHolder.background.setBackgroundColor(events.get(i).getColor());

        //Set default view to invisible
        for (TextView dayView: viewHolder.days) {
            dayView.setVisibility(View.INVISIBLE);
        }

        //Sets days to visible if included in the activity's days.
        ArrayList<Integer> dayData = events.get(i).getDayData();
        for (Integer day: dayData) {
            switch (day) {
                case Calendar.SUNDAY: viewHolder.days.get(0).setVisibility((View.VISIBLE)); break;
                case Calendar.MONDAY: viewHolder.days.get(1).setVisibility((View.VISIBLE)); break;
                case Calendar.TUESDAY: viewHolder.days.get(2).setVisibility((View.VISIBLE)); break;
                case Calendar.WEDNESDAY: viewHolder.days.get(3).setVisibility((View.VISIBLE)); break;
                case Calendar.THURSDAY: viewHolder.days.get(4).setVisibility((View.VISIBLE)); break;
                case Calendar.FRIDAY: viewHolder.days.get(5).setVisibility((View.VISIBLE)); break;
                case Calendar.SATURDAY: viewHolder.days.get(6).setVisibility((View.VISIBLE)); break;
            }
        }
        return view;
    }
}

class EventViewHolder {
    GridLayout background;
    TextView name;
    TextView startTime;
    TextView endTime;
    ArrayList<TextView> days = new ArrayList<>();
}