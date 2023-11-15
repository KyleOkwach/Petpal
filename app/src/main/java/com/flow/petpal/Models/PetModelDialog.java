package com.flow.petpal.Models;

public class PetModelDialog {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private int petID;
    private String pet_name;
    private int imgid;

    public PetModelDialog(int petID, String pet_name, int imgid) {
        this.petID = petID;
        this.pet_name = pet_name;
        this.imgid = imgid;
    }

    // Pet ID
    public int getPet_ID() {
        return petID;
    }
    public void setPetID(int petID) {
        this.petID = petID;
    }

    // Pet name
    public String getPet_name() {
        return pet_name;
    }
    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    // Pet image
    public int getImgid() {
        return imgid;
    }
    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}