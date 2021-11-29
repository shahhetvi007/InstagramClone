package com.example.instagramclone.liked;

public class RequestData {
    private int profile;
    private String request;

    public RequestData() {
    }

    public RequestData(int profile, String request) {
        this.profile = profile;
        this.request = request;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
