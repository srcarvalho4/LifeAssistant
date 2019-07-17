package utils;

import android.graphics.Color;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;

public class Activity {

    Color color;
    String typeName;
    ArrayList<Rule> rules;

    public Activity(Color color, String typeName, ArrayList<Rule> rules) {
        this.color = color;
        this.typeName = typeName;
        this.rules = rules;
    }

    //Add a rule to the list of rules
    public void addRule(Rule r) {
        this.rules.add(r);
    }
}
