package com.dimitri.remoiville.go4lunch.model;

import java.util.List;

public class RestaurantFirestore {

    private String mPlaceId;
    private String mName;
    private List<User> mUsers;

    public RestaurantFirestore() {

    }

    public RestaurantFirestore(String placeId, String name, List<User> users) {
        mPlaceId = placeId;
        mName = name;
        mUsers = users;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }
}
