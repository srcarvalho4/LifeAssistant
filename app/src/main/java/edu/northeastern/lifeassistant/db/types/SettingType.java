package edu.northeastern.lifeassistant.db.types;

public enum SettingType {

    RINGER("Ringer"),
    STEP_COUNT("Step Count"),
    DRIVING_MODE("Driving Mode"),
    NIGHT_MODE("Night Mode");

    private String value;

    SettingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
