package utils;

import android.app.UiModeManager;
import android.content.Context;
import android.media.AudioManager;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.lifeassistant.db.models.RuleDb;

public class NightModeRule implements Rule {

    UiModeManager uiModeManager;
    //Value can be NIGHT_MODE_NO, NIGHT_MODE_YES, NIGHT_MODE_AUTO
    int desiredValue;
    int previousValue;

    public NightModeRule(Context context, int desiredValue) {
        uiModeManager =(UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        this.desiredValue = desiredValue;
        this.previousValue = UiModeManager.MODE_NIGHT_NO;
    }

    public NightModeRule(Context context, RuleDb db) {
        this(context, db.getSettingValue());
    }

    @Override
    public void enable() {
        this.previousValue = uiModeManager.getNightMode();
        uiModeManager.setNightMode(desiredValue);
    }

    @Override
    public void disable() {
        uiModeManager.setNightMode(previousValue);
    }

    @Override
    public String getName() {
        return "NightMode";
    }

    @Override
    public List<Pair<Integer, String>> getSettingValues() {
        List<Pair<Integer, String>> values = new ArrayList<>();
        values.add(new Pair(UiModeManager.MODE_NIGHT_YES, "On"));
        values.add(new Pair(UiModeManager.MODE_NIGHT_NO, "Off"));
        return values;
    }

    @Override
    public int getSetting() {
        return desiredValue;
    }
}
