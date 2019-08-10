package edu.northeastern.lifeassistant.utils.items;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.AppDatabase;
import edu.northeastern.lifeassistant.db.models.ActivityDb;
import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.utils.rules.DrivingModeRule;
import edu.northeastern.lifeassistant.utils.rules.NightModeRule;
import edu.northeastern.lifeassistant.utils.rules.RingerRule;
import edu.northeastern.lifeassistant.utils.rules.Rule;

public class Activity {

    int color;
    String name;
    ArrayList<Rule> rules;

    //used to construct an activity with data directly
    public Activity(int color, String name, ArrayList<Rule> rules) {
        this.color = color;
        this.name = name;
        this.rules = rules;
    }

    //used to construct an activity using the database reference
    public Activity(Context applicationContext, String activityID) {
        ArrayList<Rule> rules = new ArrayList<>();

        AppDatabase db = AppDatabase.getAppDatabase(applicationContext);
        ActivityDb activityDb = db.activityDao().findActivityById(activityID);

        List<RuleDb> dbRules = db.ruleDao().findRulesForActivity(activityID);

        //construct rule items from the database entries
        for (RuleDb rule : dbRules) {
            Rule newRule;

            switch (rule.getSetting()) {
                case DRIVING_MODE: newRule = new DrivingModeRule(applicationContext, rule.getSettingValue()); break;
                case NIGHT_MODE: newRule = new NightModeRule(applicationContext, rule.getSettingValue()); break;
                case RINGER: newRule = new RingerRule(applicationContext, rule.getSettingValue()); break;
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
