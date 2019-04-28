package com.mmanchala.coen268.taskit.Model;

public class Task {

    private String title;
    private String date;
    private String id;
    private String groupID;
    private String state;
    private String assignedTo;
    private String timeTaking;
    public Task(){

    }



    public Task(String title, String date, String id, String timeTaking, String groupID, String state, String assignedTo) {
        this.title = title;
        this.date = date;
        this.id = id;
        this.timeTaking = timeTaking;
        this.groupID = groupID;
        this.state = state;
        this.assignedTo =assignedTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTimeTaking() {
        return timeTaking;
    }
    public void setTimeTaking(String timeTaking) {
        this.timeTaking = timeTaking;
    }

}
