package edu.northeastern.lifeassistant.utils.items;

public class HistoryAdapterInfoItem {

    public HistoryAdapterInfoItem(int imageResID, String activityName, String stepCount, String startTime, String endTime, String activityId, String entryId) {
        this.imageResID = imageResID;
        this.activityName = activityName;
        this.stepCount = stepCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityId = activityId;
        this.entryId = entryId;
    }

    public int getImageResID() {
        return imageResID;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getStepCount() {
        return stepCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setImageResID(int imageResID) {
        this.imageResID = imageResID;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    int imageResID;
    String activityName;
    String stepCount;
    String startTime;
    String endTime;

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    String activityId;

    public String getEntryId() {
        return entryId;
    }

    String entryId;

}
