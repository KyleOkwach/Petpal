package com.flow.petpal.Models;

public class EventModel {

    String eventID, eventDate, eventDescription, petID;

    public EventModel() {}

    public EventModel(String eventID, String eventDate, String eventDescription, String petID) {
        this.eventID = eventID;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.petID = petID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getPetID() {
        return petID;
    }
}
