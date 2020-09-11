package com.dimitri.remoiville.go4lunch.model;

public class Workmate {

    private long userID;
    private String mLastName;
    private String mFirstName;
    private String mUsername;
    private int mProfilePicture;

    public Workmate(long userID, String lastName, String firstName, String username, int profilePicture) {
        this.userID = userID;
        mLastName = lastName;
        mFirstName = firstName;
        mUsername = username;
        mProfilePicture = profilePicture;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public int getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        mProfilePicture = profilePicture;
    }
}
