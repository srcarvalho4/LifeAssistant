package utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.lifeassistant.db.models.RuleDb;
import edu.northeastern.lifeassistant.db.types.SettingType;

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
        Log.d("setAlarm", "ringer rule enabled!");
        previousState = audioManager.getRingerMode();
        audioManager.setRingerMode(ruleState);
    }

    @Override
    public void disable() {
        audioManager.setRingerMode(previousState);
    }

    @Override
    public String getName() {
        return SettingType.RINGER.getValue();
    }

    @Override
    public List<Pair<Integer, String>> getSettingValues() {
        List<Pair<Integer, String>> values = new ArrayList<>();
        values.add(new Pair(AudioManager.RINGER_MODE_SILENT, "On"));
        values.add(new Pair(AudioManager.RINGER_MODE_NORMAL, "Off"));
        values.add(new Pair(AudioManager.RINGER_MODE_VIBRATE, "Vibrate"));
        return values;
    }

    @Override
    public int getSetting() {
        return ruleState;
    }
}
