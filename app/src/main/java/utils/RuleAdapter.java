package utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.lifeassistant.R;

public class RuleAdapter extends BaseAdapter {

    Context context;
    ArrayList<RuleAdapterItem> rules = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();

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

    public Map<Integer, String> getRules() {
        Map<Integer, String> ruleValues = new HashMap<>();
        for (int i = 0; i < rules.size(); i++) {
            ruleValues.put(values.get(i), rules.get(i).getName());
        }
        return ruleValues;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        RuleViewHolder viewHolder;

        if (view == null) {
            view = View.inflate(context, R.layout.list_rule_item, null);

            viewHolder = new RuleViewHolder();

            viewHolder.textView = view.findViewById(R.id.RuleItemName);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (RuleViewHolder) view.getTag();
        }


        viewHolder.textView.setText(rules.get(i).getName());


        return view;
    }
}

class RuleViewHolder {
    TextView textView;
    Button button1;
    Button button2;
    Button button3;
}