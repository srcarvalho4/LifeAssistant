package edu.northeastern.lifeassistant.utils.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.R;
import edu.northeastern.lifeassistant.utils.items.RuleAdapterItem;

public class RuleAdapter extends BaseAdapter {

    Context context;
    ArrayList<RuleAdapterItem> rules;
    boolean editable;

    public RuleAdapter(Context context, ArrayList<RuleAdapterItem> rules, boolean editable) {
        this.context = context;
        this.rules = rules;
        this.editable = editable;
    }

    @Override
    public int getCount() {
        return rules.size();
    }

    @Override
    public RuleAdapterItem getItem(int i) {
        return rules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateData(ArrayList<RuleAdapterItem> rules) {
        this.rules = rules;
        notifyDataSetChanged();
    }

    public List<RuleAdapterItem> getRules() {
        return rules;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        RuleViewHolder viewHolder;

        //viewholder setup
        if (view == null) {
            view = View.inflate(context, R.layout.list_rule_item, null);

            viewHolder = new RuleViewHolder();

            //set up the viewholder
            viewHolder.textView = view.findViewById(R.id.RuleItemName);
            viewHolder.button1 = view.findViewById(R.id.RuleItemButton1);
            viewHolder.button2 = view.findViewById(R.id.RuleItemButton2);
            viewHolder.button3 = view.findViewById(R.id.RuleItemButton3);
            viewHolder.deleteButton = view.findViewById(R.id.RuleItemDelete);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (RuleViewHolder) view.getTag();
        }


        viewHolder.textView.setText(rules.get(i).getName());

        //data is a list of pairs. the pairs are the enum value for a setting matched with its name
        List<Pair<Integer, String>> settings = rules.get(i).getSettings();

        //assign the name of the setting values to the buttons
        viewHolder.button1.setText(settings.get(0).second);
        viewHolder.button2.setText(settings.get(1).second);

        //enable the third button only if it is needed
        if (settings.size() == 3) {
            viewHolder.button3.setVisibility(View.VISIBLE);
            viewHolder.button3.setText(settings.get(2).second);
        }
        else {
            viewHolder.button3.setVisibility(View.GONE);
        }

        //convert between the saved setting value and the rule option's index in the list<pair>
        int selectedNum = -1;
        for(int j = 0; j < rules.get(i).getSettings().size(); j++) {
            if (rules.get(i).getSettings().get(j).first == rules.get(i).getValue()) {
                selectedNum = j;
            }
        }

        //set all buttons to white except the one currently selected
        viewHolder.button1.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
        viewHolder.button2.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
        viewHolder.button3.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
        switch (selectedNum) {
            case 0: viewHolder.button1.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary)); break;
            case 1: viewHolder.button2.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary)); break;
            case 2: viewHolder.button3.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary)); break;
        }

        if (editable) {
            //update visuals and internal data when a button is pressed. The buttons act as radio buttons internally
            viewHolder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rules.get(i).setValue(settings.get(0).first);
                    viewHolder.button1.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                    viewHolder.button2.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                    viewHolder.button3.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                }
            });
            viewHolder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rules.get(i).setValue(settings.get(1).first);
                    viewHolder.button1.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                    viewHolder.button2.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                    viewHolder.button3.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                }
            });
            viewHolder.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rules.get(i).setValue(settings.get(2).first);
                    viewHolder.button1.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                    viewHolder.button2.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.lightGrey));
                    viewHolder.button3.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                }
            });

            //bind the delete button which removes the rule
            viewHolder.deleteButton.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rules.remove(i);
                    notifyDataSetChanged();
                }
            });
        }
        else {
            //delete button is invisible and buttons are not functional when not on an editable screen
            viewHolder.deleteButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    class RuleViewHolder {
        TextView textView;
        Button button1;
        Button button2;
        Button button3;
        ImageButton deleteButton;
    }
}
