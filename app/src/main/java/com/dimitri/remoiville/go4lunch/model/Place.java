package com.dimitri.remoiville.go4lunch.model;

import java.util.List;
import java.util.Objects;

public class Place {

    private String placeId;
    private String mName;
    private String mType;
    private String mAddress;
    private int mDistance;
    private int mRating;
    private String mUrlPicture;
    private List<Workmate> mWorkmateList;
    private Boolean mOpen;
    private String mPhoneNumbers;


    public Place(String placeId, String name, String type, String address, int distance, int rating, String urlPicture, List<Workmate> workmateList, Boolean open, String phoneNumbers) {
        this.placeId = placeId;
        this.mName = name;
        this.mType = type;
        this.mAddress = address;
        this.mDistance = distance;
        this.mRating = rating;
        this.mUrlPicture = urlPicture;
        this.mWorkmateList = workmateList;
        this.mOpen = open;
        this.mPhoneNumbers = phoneNumbers;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
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

    public String getUrlPicture() {
        return mUrlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        mUrlPicture = urlPicture;
    }

    public List<Workmate> getWorkmateList() {
        return mWorkmateList;
    }

    public void setWorkmateList(List<Workmate> workmateList) {
        mWorkmateList = workmateList;
    }

    public Boolean getOpen() {
        return mOpen;
    }

    public void setOpen(Boolean open) {
        mOpen = open;
    }

    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Place place = (Place) o;
        return Objects.equals(placeId, place.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId);
    }
}
