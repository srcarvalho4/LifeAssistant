package utils;

import android.app.UiModeManager;
import android.content.Context;

public class DrivingModeRule implements Rule {

    UiModeManager uiModeManager;
    //0 off, 1 on
    int desiredValue;

    public DrivingModeRule(Context context, int desiredValue) {
        uiModeManager =(UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        this.desiredValue = desiredValue;
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
        return "DrivingMode";
    }
}
