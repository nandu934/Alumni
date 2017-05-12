package com.example.user.alumni.models;

/**
 * Created by User on 06-04-2017.
 */

public class VolExpData {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;
    public String organisation;
    public String role;
    public String cause;
    public String sdate;
    public String edate;

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description;

}
