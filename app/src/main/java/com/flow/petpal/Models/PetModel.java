package com.flow.petpal.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PetModel implements Parcelable {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String petID, pet_name, ownerID, petImageUri, petDOB, petSpecies, petGender;

    public PetModel(String petID, String pet_name, String ownerID, String petImageUri, String petDOB, String petSpecies, String petGender) {
        this.petID = petID;
        this.pet_name = pet_name;
        this.ownerID = ownerID;
        this.petImageUri = petImageUri;
        this.petDOB = petDOB;
        this.petSpecies = petSpecies;
        this.petGender = petGender;
    }

    protected PetModel(Parcel in) {
        petID = in.readString();
        pet_name = in.readString();
        ownerID = in.readString();
        petImageUri = in.readString();
        petDOB = in.readString();
        petSpecies = in.readString();
        petGender = in.readString();
    }

    public static final Creator<PetModel> CREATOR = new Creator<PetModel>() {
        @Override
        public PetModel createFromParcel(Parcel in) {
            return new PetModel(in);
        }

        @Override
        public PetModel[] newArray(int size) {
            return new PetModel[size];
        }
    };

    // Pet ID
    public String getPet_ID() {
        return petID;
    }
    public void setPetID(String petID) {
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
    public String getPetImageUri() {
        return petImageUri;
    }

    public void setPetImageUri(String petImageUri) {
        this.petImageUri = petImageUri;
    }

    public String getPetDOB() {
        return petDOB;
    }

    public void setPetDOB(String petDOB) {
        this.petDOB = petDOB;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(petID);
        dest.writeString(pet_name);
        dest.writeString(ownerID);
        dest.writeString(petImageUri);
        dest.writeString(petDOB);
        dest.writeString(petSpecies);
        dest.writeString(petGender);
    }
}