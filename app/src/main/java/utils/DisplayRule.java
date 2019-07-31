package utils;


import edu.northeastern.lifeassistant.db.models.RuleDb;

//This class is only for UI work. Not to be kept.
public class DisplayRule implements Rule {

    String name;

    public DisplayRule(String name) {
        this.name = name;
    }

    public DisplayRule(RuleDb db) {
        this.name = db.getSetting().getValue();
        this.name = this.name.substring(0,1).toUpperCase() + this.name.substring(1);
        for (int i = 0; i < name.length()-1; i++) {
            if (name.charAt(i) == '_') {
                name = name.substring(0,i) + " " + name.substring(i+1, i+2).toUpperCase() + name.substring(i+2);
            }
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

    @Override
    public String getName() {
        return name;
    }
}
