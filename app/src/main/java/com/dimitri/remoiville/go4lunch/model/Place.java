package com.dimitri.remoiville.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Set;

public class Place {

    private long placeId;
    private String mName;
    private String mType;
    private String mAddress;
    private int mDistance;
    private int mRating;
    @Nullable
    private String mUrlPicture;
    private List<Workmate> mWorkmateList;
    private Set<OpeningHours> mOpeningHours;

    public Place(long placeId, String name, String type, String address, int distance, int rating, String urlPicture, List<Workmate> workmateList, Set<OpeningHours> openingHours) {
        this.placeId = placeId;
        mName = name;
        mType = type;
        mAddress = address;
        mDistance = distance;
        mRating = rating;
        mUrlPicture = urlPicture;
        mWorkmateList = workmateList;
        mOpeningHours = openingHours;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    @Nullable
    public String getUrlPicture() {
        return mUrlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        mUrlPicture = urlPicture;
    }

    public List<Workmate> getWorkmateList() {
        return mWorkmateList;
    }

    public void setWorkmateList(List<Workmate> workmateList) {
        mWorkmateList = workmateList;
    }

    public Set<OpeningHours> getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(Set<OpeningHours> openingHours) {
        mOpeningHours = openingHours;
    }
}
