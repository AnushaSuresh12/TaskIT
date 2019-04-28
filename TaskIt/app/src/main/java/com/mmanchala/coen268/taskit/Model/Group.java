package com.mmanchala.coen268.taskit.Model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private String created_by;
    private String id;
    private ArrayList<String> groupMembers;

    public Group(){

    }

    public Group(String groupName, String created_by, String id, ArrayList<String> groupMembers) {
        this.groupName = groupName;
        this.created_by = created_by;
        this.id = id;
        this.groupMembers = groupMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }


}
