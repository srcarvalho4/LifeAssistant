package edu.northeastern.lifeassistant.utils.items;

public class ColorPicker {

    boolean isSelected;
    int color;

    public ColorPicker(int color, boolean isSelected) {
        this.color = color;
        this.isSelected = isSelected;
    }

    public int getColor() {
        return color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
