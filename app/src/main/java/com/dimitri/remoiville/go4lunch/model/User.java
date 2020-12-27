package com.dimitri.remoiville.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String mUserID;
    private String mFirstName;
    private String mLastName;
    private String mMail;
    private String mURLProfilePicture;
    private List<String> mLikesList;
    private String mRestaurantID;
    private String mRestaurantName;
    private boolean mHasChosenNotification;

    private final String defaultURLPicture = "https://firebasestorage.googleapis.com/v0/b/go4lunch-ee885.appspot.com/o/avatars%2Fdefault%2Fdefault_picture.jpg?alt=media&token=8c100d65-cb26-4c6f-8319-470ac3627c42";

    public User() {
    }

    public User(String userID, String firstName, String lastName, String mail, String photoURL) {
        mUserID = userID;
        mFirstName = firstName;
        mLastName = lastName;
        mMail = mail;
        if (photoURL == null) {
            mURLProfilePicture = randomImage();
        } else {
            mURLProfilePicture = photoURL;
        }
        mLikesList = new ArrayList<>();
        mRestaurantID = null;
        mRestaurantName = null;
        mHasChosenNotification = false;
    }


    public User(String userID, String firstName, String lastName, String mail, String URLProfilePicture, List<String> likesList, String restaurantID, String restaurantName, boolean hasChosenNotification) {
        mUserID = userID;
        mFirstName = firstName;
        mLastName = lastName;
        mMail = mail;
        mURLProfilePicture = URLProfilePicture;
        mLikesList = likesList;
        mRestaurantID = restaurantID;
        mRestaurantName = restaurantName;
        mHasChosenNotification = hasChosenNotification;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
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

    public String getRestaurantID() {
        return mRestaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.mRestaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }

    public boolean hasChosenNotification() {
        return mHasChosenNotification;
    }

    public void setHasChosenNotification(boolean hasChosenNotification) {
        mHasChosenNotification = hasChosenNotification;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserID='" + mUserID + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mMail='" + mMail + '\'' +
                ", mURLProfilePicture='" + mURLProfilePicture + '\'' +
                ", mLikesList=" + mLikesList +
                ", mLunchRestaurantID='" + mRestaurantID + '\'' +
                ", mRestaurantName='" + mRestaurantName + '\'' +
                '}';
    }

    private String randomImage() {
        return "https://i.pravatar.cc/300?u="+ System.currentTimeMillis();
    }
}
