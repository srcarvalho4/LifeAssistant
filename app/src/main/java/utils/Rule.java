package utils;

import java.util.Map;

public interface Rule {

    //Saves current state of settings and sets new settings.
    void enable();

    //Returns settings to original state.
    void disable();

    //Returns the name of the rule for display purposes
    String getName();

    //Returns a map of display values to functional values
    Map<Integer, String> getSettingValues();

    //Returns the current setting
    int getSetting();
}
