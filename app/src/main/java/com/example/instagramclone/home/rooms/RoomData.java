package com.example.instagramclone.home.rooms;

public class RoomData {
    private int image;
    private String personAccountName, personName;

    public RoomData() {
    }

    public RoomData(int image, String personAccountName, String personName) {
        this.image = image;
        this.personAccountName = personAccountName;
        this.personName = personName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPersonAccountName() {
        return personAccountName;
    }

    public void setPersonAccountName(String personAccountName) {
        this.personAccountName = personAccountName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
