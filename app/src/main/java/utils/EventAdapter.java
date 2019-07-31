package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.dpro.widgets.WeekdaysPicker;
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

    public void updateData(ArrayList<ScheduleEvent> events) {
        this.events = events;
        notifyDataSetChanged();
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
            viewHolder.dayPicker = view.findViewById(R.id.eventItemDayPicker);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (EventViewHolder) view.getTag();
        }

        viewHolder.name.setText(events.get(i).getName());
        viewHolder.startTime.setText(events.get(i).getStartTimeText());
        viewHolder.endTime.setText(events.get(i).getEndTimeText());
        viewHolder.background.setBackgroundColor(events.get(i).getColor());
        viewHolder.dayPicker.setSelectedDays(events.get(i).getDayData());
        int color = events.get(i).getColor();
        int darkColorR = (int) (((color >> 16) & 0xff) * 3 / 4);
        int darkColorG = (int) (((color >>  8) & 0xff) * 3 / 4);
        int darkColorB = (int) (((color) & 0xff) * 3 / 4);
        int darkColor = Color.rgb(darkColorR, darkColorG, darkColorB);
        int lightColorR = (int) (((((color >> 16) & 0xff) - 255) / 2) + 255);
        int lightColorG = (int) (((((color >>  8) & 0xff) - 255) / 2) + 255);
        int lightColorB = (int) (((((color) & 0xff) - 255) / 2) + 255);
        int lightColor = Color.rgb(lightColorR, lightColorG, lightColorB);

//        viewHolder.startTime.setTextColor(darkColor);
//        viewHolder.endTime.setTextColor(lightColor);
        viewHolder.dayPicker.setHighlightColor(darkColor);
        viewHolder.dayPicker.setTextColor(lightColor);

//        //Set default view to invisible
//        for (TextView dayView: viewHolder.days) {
//            dayView.setVisibility(View.INVISIBLE);
//        }
//
//        //Sets days to visible if included in the activity's days.
//        ArrayList<Integer> dayData = events.get(i).getDayData();
//        for (Integer day: dayData) {
//            switch (day) {
//                case Calendar.SUNDAY: viewHolder.days.get(0).setVisibility((View.VISIBLE)); break;
//                case Calendar.MONDAY: viewHolder.days.get(1).setVisibility((View.VISIBLE)); break;
//                case Calendar.TUESDAY: viewHolder.days.get(2).setVisibility((View.VISIBLE)); break;
//                case Calendar.WEDNESDAY: viewHolder.days.get(3).setVisibility((View.VISIBLE)); break;
//                case Calendar.THURSDAY: viewHolder.days.get(4).setVisibility((View.VISIBLE)); break;
//                case Calendar.FRIDAY: viewHolder.days.get(5).setVisibility((View.VISIBLE)); break;
//                case Calendar.SATURDAY: viewHolder.days.get(6).setVisibility((View.VISIBLE)); break;
//            }
//        }
        return view;
    }
}

class EventViewHolder {
    GridLayout background;
    TextView name;
    TextView startTime;
    TextView endTime;
    WeekdaysPicker dayPicker;
}