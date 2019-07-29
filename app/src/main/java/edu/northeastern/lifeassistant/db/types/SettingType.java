package edu.northeastern.lifeassistant.db.types;

public enum SettingType {

    VOLUME("volume"),
    STEP_COUNT("step_count"),
    DRIVING_MODE("driving_mode"),
    NIGHT_MODE("night_mode");

    private String value;

    SettingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
