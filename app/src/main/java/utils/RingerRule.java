package utils;

import android.media.AudioManager;

public class RingerRule implements Rule {

    int previousState;
    int ruleState;
    AudioManager audioManager;

    public RingerRule(int desiredRingerMode) {
        //default
        this.previousState = AudioManager.ADJUST_MUTE;
        //audioManager = new AudioManager(); -> says constructor can't be used???
        this.ruleState = desiredRingerMode;
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
        return "Sound";
    }
}
