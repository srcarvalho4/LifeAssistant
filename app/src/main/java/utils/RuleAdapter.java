package utils;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.R;

public class RuleAdapter extends BaseAdapter {

    Context context;
    ArrayList<RuleAdapterItem> rules;

    public RuleAdapter(Context context, ArrayList<RuleAdapterItem> rules) {
        this.context = context;
        this.rules = rules;
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

        if (view == null) {
            view = View.inflate(context, R.layout.list_rule_item, null);

            viewHolder = new RuleViewHolder();

            viewHolder.textView = view.findViewById(R.id.RuleItemName);
            viewHolder.button1 = view.findViewById(R.id.RuleItemButton1);
            viewHolder.button2 = view.findViewById(R.id.RuleItemButton2);
            viewHolder.button3 = view.findViewById(R.id.RuleItemButton3);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (RuleViewHolder) view.getTag();
        }


        viewHolder.textView.setText(rules.get(i).getName());
        List<Pair<Integer, String>> settings = rules.get(i).getSettings();

        viewHolder.button1.setText(settings.get(0).second);
        viewHolder.button1.setText(settings.get(1).second);
        if (settings.size() == 3) {
            viewHolder.button3.setVisibility(View.VISIBLE);
            viewHolder.button3.setText(settings.get(2).second);
        }
        else {
            viewHolder.button3.setVisibility(View.GONE);
        }


        viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rules.get(i).setValue(settings.get(0).first);
            }
        });
        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rules.get(i).setValue(settings.get(1).first);
            }
        });
        viewHolder.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rules.get(i).setValue(settings.get(2).first);
            }
        });

        return view;
    }
}

class RuleViewHolder {
    TextView textView;
    Button button1;
    Button button2;
    Button button3;
}