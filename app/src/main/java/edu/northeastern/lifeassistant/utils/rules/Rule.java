package edu.northeastern.lifeassistant.utils.rules;

import android.util.Pair;

import java.util.List;

public interface Rule {

    //Saves current state of settings and sets new settings.
    void enable();

    //Returns settings to original state.
    void disable();

    //Returns the name of the rule for display purposes
    String getName();

    //Returns a map of display values to functional values
    List<Pair<Integer, String>> getSettingValues();

    //Returns the current setting
    int getSetting();
}
