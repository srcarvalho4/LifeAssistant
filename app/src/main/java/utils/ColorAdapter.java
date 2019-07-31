package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.db.AppDatabase;

public class ColorAdapter extends BaseAdapter {

    final Context context;
    ArrayList<ColorPicker> colorPickers;

    public ColorAdapter(Context context, ArrayList<ColorPicker> colorPickers) {
        this.context = context;
        this.colorPickers = colorPickers;
    }

    @Override
    public int getCount() {
        return colorPickers.size();
    }

    @Override
    public Object getItem(int i) {
        return colorPickers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateData(ArrayList<ColorPicker> colorPickers) {
        this.colorPickers = colorPickers;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ColorAdapterHolder viewHolder;

        if (view == null) {
            view = View.inflate(context, R.layout.color_picker_item, null);

            viewHolder = new ColorAdapterHolder();

            viewHolder.button = view.findViewById(R.id.colorPickerButton);
            viewHolder.imageView = view.findViewById(R.id.colorPickerSelectedView);



            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ColorAdapterHolder) view.getTag();
        }


        viewHolder.button.setBackgroundColor(colorPickers.get(i).getColor());
        if (colorPickers.get(i).isSelected()) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.imageView.setVisibility(View.INVISIBLE);
        }

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < colorPickers.size(); j++) {
                    colorPickers.get(j).setSelected(false);
                }
                colorPickers.get(i).setSelected(true);
                updateData(colorPickers);
            }
        });

        return view;
    }

    public int getCurrentColor() {
        for (int i = 0; i < colorPickers.size(); i++) {
            if (colorPickers.get(i).isSelected()) {
                return colorPickers.get(i).getColor();
            }
        }
        return Color.GRAY;
    }
}

class ColorAdapterHolder {
    Button button;
    ImageView imageView;
}