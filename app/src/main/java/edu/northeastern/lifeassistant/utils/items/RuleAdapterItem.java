package edu.northeastern.lifeassistant.utils.items;

import android.util.Pair;

import java.util.List;

import edu.northeastern.lifeassistant.utils.rules.Rule;

public class RuleAdapterItem {
    String name;
    int value;
    List<Pair<Integer, String>> settings;

    public RuleAdapterItem(Rule rule) {
        this.name = rule.getName();
        this.value = rule.getSetting();
        this.settings = rule.getSettingValues();
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public List<Pair<Integer, String>> getSettings() {
        return settings;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof RuleAdapterItem)) {
            return false;
        }

        return ((RuleAdapterItem) obj).getName().equals(this.getName());
    }
}
