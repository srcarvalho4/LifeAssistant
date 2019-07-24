package utils;

import android.app.UiModeManager;
import android.content.Context;

public class DrivingModeRule implements Rule {

    UiModeManager uiModeManager;
    boolean desiredValue;

    public DrivingModeRule(Context context, boolean desiredValue) {
        uiModeManager =(UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        this.desiredValue = desiredValue;
    }

    @Override
    public void enable() {
        if (desiredValue) {
            uiModeManager.enableCarMode(0);
        } else {
            uiModeManager.disableCarMode(0);
        }
    }

    @Override
    public void disable() {
        if (desiredValue) {
            uiModeManager.disableCarMode(0);
        } else {
            uiModeManager.enableCarMode(0);
        }
    }

    @Override
    public String getName() {
        return "DrivingMode";
    }
}
