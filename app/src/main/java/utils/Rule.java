package utils;

import java.util.Map;

public interface Rule {

    //Saves current state of settings and sets new settings.
    void enable();

    //Returns settings to original state.
    void disable();

    //Returns the name of the rule for display purposes
    String getName();

//    Map<Integer, String> getSettingValue();
}
