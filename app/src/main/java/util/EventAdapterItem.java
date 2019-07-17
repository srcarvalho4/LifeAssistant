package util;

public class EventAdapterItem {
    int days;
    String name;
    String startTime;
    String endTime;
    int color;

    public EventAdapterItem(String name, String startTime, String endTime, int color, int days) {
        this.days = days;
        this.color = color;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDayData() {
        return days;
    }
}
