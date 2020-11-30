package com.dimitri.remoiville.go4lunch.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String mUserID;
    private String mName;
    private String mMail;
    private String mURLProfilePicture;
    private List<String> mLikesList;
    private String mLunchRestaurantID;
    private Boolean mIsLunchBooked;

    private String defaultURLPicture = "https://firebasestorage.googleapis.com/v0/b/go4lunch-63219/o/default_picture.png?alt=media&token=421a8985-e6ce-4d7c-a0cb-da1d2bc93799";

    public User() {
    }

    public User(String userID, String name, String mail) {
        mUserID = userID;
        mName = name;
        mMail = mail;
        mURLProfilePicture = defaultURLPicture;
        mLikesList = new ArrayList<>();
        mLunchRestaurantID = "";
        mIsLunchBooked = false;
    }

    public User(String userID, String name, String mail, String URLProfilePicture) {
        mUserID = userID;
        mName = name;
        mMail = mail;
        mURLProfilePicture = URLProfilePicture;
        mLikesList = new ArrayList<>();
        mLunchRestaurantID = "";
        mIsLunchBooked = false;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMail() {
        return mMail;
    }

    public void setMail(String mail) {
        mMail = mail;
    }

    public String getURLProfilePicture() {
        return mURLProfilePicture;
    }

    public void setURLProfilePicture(String URLProfilePicture) {
        mURLProfilePicture = URLProfilePicture;
    }

    public List<String> getLikesList() {
        return mLikesList;
    }

    public void setLikesList(List<String> likesList) {
        mLikesList = likesList;
    }

    public String getLunchRestaurantID() {
        return mLunchRestaurantID;
    }

    public void setLunchRestaurantID(String lunchRestaurantID) {
        this.mLunchRestaurantID = lunchRestaurantID;
    }

    public Boolean getLunchBooked() {
        return mIsLunchBooked;
    }

    public void setLunchBooked(Boolean lunchBooked) {
        mIsLunchBooked = lunchBooked;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "mUserID='" + mUserID + '\'' +
                ", mName='" + mName + '\'' +
                ", mMail='" + mMail + '\'' +
                ", mURLProfilePicture='" + mURLProfilePicture + '\'' +
                ", mLikesList=" + mLikesList +
                ", lunchRestaurantID='" + mLunchRestaurantID + '\'' +
                ", isLunchBooked=" + mIsLunchBooked +
                '}';
    }
}
