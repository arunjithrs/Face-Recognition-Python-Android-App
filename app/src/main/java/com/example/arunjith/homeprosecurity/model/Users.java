package com.example.arunjith.homeprosecurity.model;

public class Users {
    String name;
    boolean access;
    String pro_pic;

    public Users(String name, boolean access, String pro_pic) {
        this.name = name;
        this.access = access;
        this.pro_pic = pro_pic;
    }

    public String getName() {
        return name;
    }

    public boolean isAccess() {
        return access;
    }

    public String getPro_pic() {
        return pro_pic;
    }
}
