package edu.northeastern.lifeassistant.utils.rules;

import android.app.UiModeManager;
import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.types.SettingType;

public class DrivingModeRule implements Rule {

    UiModeManager uiModeManager;
    //0 off, 1 on
    int desiredValue;

    public DrivingModeRule(Context context, int desiredValue) {
        uiModeManager =(UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        this.desiredValue = desiredValue;
    }

    public DrivingModeRule(Context context, RuleDb db) {
        this(context, db.getSettingValue());
    }

    @Override
    public void enable() {
        if (desiredValue == 1) {
            uiModeManager.enableCarMode(0);
        } else {
            uiModeManager.disableCarMode(0);
        }
    }

    @Override
    public void disable() {
        if (desiredValue == 1) {
            uiModeManager.disableCarMode(0);
        } else {
            uiModeManager.enableCarMode(0);
        }
    }

    @Override
    public String getName() {
        return SettingType.DRIVING_MODE.getValue();
    }

    @Override
    public int getSetting() {
        return desiredValue;
    }

    @Override
    public List<Pair<Integer, String>> getSettingValues() {
        List<Pair<Integer, String>> values = new ArrayList<>();
        values.add(new Pair(1, "On"));
        values.add(new Pair(0, "Off"));
        return values;
    }
}
