package edu.northeastern.lifeassistant.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.dpro.widgets.WeekdaysPicker;
import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.utils.items.ScheduleEvent;

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

        //viewholder setup
        if (view == null) {
            view = View.inflate(context, R.layout.list_event_item_1, null);

            viewHolder = new EventViewHolder();

            viewHolder.background = view.findViewById(R.id.cardViewScheduleScreen1);
            viewHolder.name = view.findViewById(R.id.eventItemName);
            viewHolder.startTime = view.findViewById(R.id.eventItemStartTime);
            viewHolder.endTime = view.findViewById(R.id.eventItemEndTime);
            viewHolder.dayPicker = view.findViewById(R.id.eventItemDayPicker);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (EventViewHolder) view.getTag();
        }

        //value changing
        viewHolder.name.setText(events.get(i).getName());
        viewHolder.startTime.setText(events.get(i).getStartTimeText());
        viewHolder.endTime.setText(events.get(i).getEndTimeText());
        viewHolder.background.setBackgroundColor(events.get(i).getColor());
        viewHolder.dayPicker.setSelectedDays(events.get(i).getDayData());

        //color modification code
//        int color = events.get(i).getColor();
//        int darkColorR = (int) (((color >> 16) & 0xff) * 3 / 4);
//        int darkColorG = (int) (((color >>  8) & 0xff) * 3 / 4);
//        int darkColorB = (int) (((color) & 0xff) * 3 / 4);
//        int darkColor = Color.rgb(darkColorR, darkColorG, darkColorB);
//        int lightColorR = (int) (((((color >> 16) & 0xff) - 255) / 2) + 255);
//        int lightColorG = (int) (((((color >>  8) & 0xff) - 255) / 2) + 255);
//        int lightColorB = (int) (((((color) & 0xff) - 255) / 2) + 255);
//        int lightColor = Color.rgb(lightColorR, lightColorG, lightColorB);

        return view;
    }

    class EventViewHolder {
        LinearLayout background;
        TextView name;
        TextView startTime;
        TextView endTime;
        WeekdaysPicker dayPicker;
    }
}