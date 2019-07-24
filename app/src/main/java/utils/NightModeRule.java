package utils;

import android.app.UiModeManager;
import android.content.Context;

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
}
