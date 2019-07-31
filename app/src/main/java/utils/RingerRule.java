package utils;

import android.content.Context;
import android.media.AudioManager;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.lifeassistant.db.models.RuleDb;

public class RingerRule implements Rule {

    int previousState;
    int ruleState;
    AudioManager audioManager;
    Context context;

    public RingerRule(Context context, int desiredRingerMode) {
        //default
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        this.previousState = AudioManager.RINGER_MODE_SILENT;
        this.ruleState = desiredRingerMode;
        this.context = context;
    }

    public RingerRule(Context context, RuleDb db) {
        this(context, db.getSettingValue());
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
