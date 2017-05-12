package com.example.user.alumni.models;

/**
 * Created by User on 04-04-2017.
 */

public class WorkexpData {

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String id;
    public String company;
    public String title;
    public String location;
    public String startdate;
    public String enddate;
    public String description;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

