package com.flow.petpal.Models;

public class PetModel {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String course_name;
    private int imgid;

    public PetModel(String course_name, int imgid) {
        this.course_name = course_name;
        this.imgid = imgid;
    }

    public String getPet_name() {
        return course_name;
    }

    public void setPet_name(String course_name) {
        this.course_name = course_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}