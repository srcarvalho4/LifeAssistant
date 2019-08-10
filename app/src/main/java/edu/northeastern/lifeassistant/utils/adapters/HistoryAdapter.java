package edu.northeastern.lifeassistant.utils.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.utils.items.HistoryAdapterInfoItem;

public class HistoryAdapter extends BaseAdapter {

    public HistoryAdapter(Context context, ArrayList<HistoryAdapterInfoItem> historyItem) {
        this.context = context;
        this.historyItem = historyItem;
    }

    Context context;
    ArrayList<HistoryAdapterInfoItem> historyItem;





    @Override
    public int getCount() {
        return historyItem.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //Initialize ViewHolder
        ViewHolder viewHolder;

        if (view == null) {
            //Inflating the listview with the design
            view  = view.inflate(context, R.layout.history_cardview, null);

            //create an object of ViewHolder
            viewHolder = new ViewHolder();

            viewHolder.imageView = view.findViewById(R.id.imageViewHistoryIcon1);
            viewHolder.textView1 = view.findViewById(R.id.textViewActivityName1);
            viewHolder.textView2 = view.findViewById(R.id.textViewStepCount1);
            viewHolder.textView3 = view.findViewById(R.id.textViewStartTime1);
            viewHolder.textView4 = view.findViewById(R.id.textViewEndTime1);
            viewHolder.textView5 = view.findViewById(R.id.textViewEntryId1);


            //Linking viewHolder to my view
            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.imageView.setImageResource(historyItem.get(i).getImageResID());
        viewHolder.textView1.setText(historyItem.get(i).getActivityName());
        viewHolder.textView2.setText(historyItem.get(i).getStepCount());
        viewHolder.textView3.setText(historyItem.get(i).getStartTime());
        viewHolder.textView4.setText(historyItem.get(i).getEndTime());
        viewHolder.textView5.setText(historyItem.get(i).getEntryId());


        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;

    }
}
