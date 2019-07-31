package utils;

import android.content.Context;
import android.graphics.Color;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;

public class Activity {

    int color;
    String name;
    ArrayList<Rule> rules;

    public Activity(int color, String name, ArrayList<Rule> rules) {
        this.color = color;
        this.name = name;
        this.rules = rules;
    }

    public Activity(Context applicationContext, String activityID) {
        ArrayList<Rule> rules = new ArrayList<>();

        AppDatabase db = AppDatabase.getAppDatabase(applicationContext);
        ActivityDb activityDb = db.activityDao().findActivityById(activityID);

        List<RuleDb> dbRules = db.ruleDao().findRulesForActivity(activityID);

        for (RuleDb rule : dbRules) {
            Rule newRule;

            switch (rule.getSetting()) {
                case DRIVING_MODE: newRule = new DrivingModeRule(applicationContext, rule.getSettingValue()); break;
                case NIGHT_MODE: newRule = new NightModeRule(applicationContext, rule.getSettingValue()); break;
                case VOLUME: newRule = new RingerRule(applicationContext, rule.getSettingValue()); break;
                default: throw new IllegalArgumentException("need a valid state type");
            }
            rules.add(newRule);
        }

        this.color = activityDb.getColor();
        this.name = activityDb.getName();
        this.rules = rules;
    }

    //Add a rule to the list of rules
    public void addRule(Rule r) {
        this.rules.add(r);
    }

    //Return this activity's color
    public int getColor() {
        return this.color;
    }

    //Return this activity's type
    public String getName(){
        return this.name;
    }

    //Return this activity's rules
    public ArrayList<Rule> getRules() {
        return this.rules;
    }
}
