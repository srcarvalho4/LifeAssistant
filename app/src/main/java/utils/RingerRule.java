package utils;

import android.media.AudioManager;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.lifeassistant.db.models.RuleDb;

public class RingerRule implements Rule {

    int previousState;
    int ruleState;
    AudioManager audioManager;

    public RingerRule(int desiredRingerMode) {
        //default
        this.previousState = AudioManager.RINGER_MODE_SILENT;
        //audioManager = new AudioManager(); -> says constructor can't be used???
        this.ruleState = desiredRingerMode;
    }

    public RingerRule(RuleDb db) {
        this(db.getSettingValue());
    }

    @Override
    public void enable() {
        previousState = audioManager.getRingerMode();
        audioManager.setRingerMode(ruleState);
    }

    @Override
    public void disable() {
        audioManager.setRingerMode(previousState);
    }

    @Override
    public String getName() {
        return "Ringer";
    }

    @Override
    public Map<Integer, String> getSettingValues() {
        Map<Integer, String> values = new HashMap<>();
        values.put(AudioManager.RINGER_MODE_SILENT, "On");
        values.put(AudioManager.RINGER_MODE_VIBRATE, "Vibrate");
        values.put(AudioManager.RINGER_MODE_NORMAL, "Off");
        return values;
    }

    @Override
    public int getSetting() {
        return ruleState;
    }
}
