package edu.northeastern.lifeassistant.db.types;

public enum SettingType {

    VOLUME("volume"),
//    LOCATION("location"),
    STEP_COUNT("step_count"),
//    BRIGHTNESS("brightness"),
//    AIRPLANE_MODE("airplane_mode"),
//    DO_NOT_DISTURB("do_not_disturb"),
//    POWER_SAVER("power_saver"),
    DRIVING_MODE("driving_mode"),
    NIGHT_MODE("night_mode");
//    BLUETOOTH("bluetooth"),
//    FLASHLIGHT("flashlight");

    private String value;

    SettingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
