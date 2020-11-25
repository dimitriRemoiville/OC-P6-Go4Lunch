package com.dimitri.remoiville.go4lunch.model;

public class User {

    private String mUserID;
    private String mLastName;
    private String mFirstName;
    private String mMail;
    private String mURLProfilePicture;

    public User(String userID, String lastName, String firstName, String mail, String URLProfilPicture) {
        this.mUserID = userID;
        mLastName = lastName;
        mFirstName = firstName;
        mMail = mail;
        mURLProfilePicture = URLProfilPicture;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        this.mUserID = userID;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
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
}
