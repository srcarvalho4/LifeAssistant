package utils;

import android.content.Context;
import android.media.AudioManager;

import edu.northeastern.lifeassistant.db.models.RuleDb;

public class RingerRule implements Rule {

    int previousState;
    int ruleState;
    AudioManager audioManager;
    Context context;

    public RingerRule(Context context, int desiredRingerMode) {
        //default
        this.previousState = AudioManager.ADJUST_MUTE;
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
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
}
