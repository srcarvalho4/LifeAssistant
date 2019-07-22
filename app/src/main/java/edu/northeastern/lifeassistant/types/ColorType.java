package edu.northeastern.lifeassistant.types;

public enum ColorType {

    RED("red", "#ef5350"),
    GREEN("green", "#66bb6a"),
    BLUE("blue", "#42a5f5"),
    PURPLE("purple", "#ab47bc"),
    YELLOW("yellow", "#ffee58"),
    ORANGE("orange", "#ffa726");

    private String name;
    private String value;

    public String getValue() {
        return this.value;
    }

    public String getString() {
        return this.name;
    }

    ColorType(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
