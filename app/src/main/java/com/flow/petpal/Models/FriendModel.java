package com.flow.petpal.Models;

public class FriendModel {
    
    String uid, action;
    
    public FriendModel() {}
    
    public FriendModel(String uid, String action) {
        this.uid = uid;
        this.action = action;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
