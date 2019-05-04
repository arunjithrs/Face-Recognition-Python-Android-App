package com.example.arunjith.homeprosecurity.model;

import java.sql.Blob;

public class Image {
    String name;
    String image;
    Boolean success;
    boolean access;
    String message;

    public Image(String name, String image, Boolean success, boolean access, String message) {
        this.name = name;
        this.image = image;
        this.success = success;
        this.access = access;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public Boolean getSuccess() {
        return success;
    }

    public boolean isAccess() {
        return access;
    }

    public String getMessage() {
        return message;
    }
}
