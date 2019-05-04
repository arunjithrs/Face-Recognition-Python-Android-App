package com.example.arunjith.homeprosecurity.model;

public class VisitorsModel {
    String name;
    String date;
    String time;
    String pic;

    public VisitorsModel() {
    }

    public VisitorsModel(String name, String date, String time, String pic) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPic() {
        return pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
