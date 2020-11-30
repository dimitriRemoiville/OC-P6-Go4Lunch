package com.dimitri.remoiville.go4lunch.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class User {

    private String mUserID;
    private String mFirstName;
    private String mLastName;
    private String mMail;
    private String mURLProfilePicture;
    private List<String> mLikesList;
    private String mLunchRestaurantID;
    private String mRestaurantName;
    private Boolean mIsLunchBooked;

    private String defaultURLPicture = "https://firebasestorage.googleapis.com/v0/b/go4lunch-63219/o/default_picture.png?alt=media&token=421a8985-e6ce-4d7c-a0cb-da1d2bc93799";

    public User() {
    }

    public User(String userID, String firstName, String lastName, String mail) {
        mUserID = userID;
        mFirstName = firstName;
        mLastName = lastName;
        mMail = mail;
        mURLProfilePicture = defaultURLPicture;
        mLikesList = new ArrayList<>();
        mLunchRestaurantID = "";
        mRestaurantName = "";
        mIsLunchBooked = false;
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

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
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
                ", mLunchRestaurantID='" + mLunchRestaurantID + '\'' +
                ", mRestaurantName='" + mRestaurantName + '\'' +
                ", mIsLunchBooked=" + mIsLunchBooked +
                '}';
    }

    public static class UserLunchIsBookedComparator implements Comparator<User> {
        @Override
        public int compare(User o1, User o2) {
            boolean b1 = o1.getLunchBooked();
            boolean b2 = o2.getLunchBooked();
            return Boolean.compare(b2,b1);
        }
    }
}
