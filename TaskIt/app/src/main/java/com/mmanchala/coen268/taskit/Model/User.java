package com.mmanchala.coen268.taskit.Model;

import java.util.List;

public class User {

    private String email;
    private String name;
    private List<String> groups;
    private String userid;

    public User(){

    }

    public User(String email, String name, List<String> groups,String userid) {
        this.email = email;
        this.name = name;
        this.groups = groups;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }



}
