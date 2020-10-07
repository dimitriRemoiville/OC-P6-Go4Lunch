package com.dimitri.remoiville.go4lunch.model;

import java.util.List;
import java.util.Objects;

public class Place {

    private String placeId;
    private String mName;
    private String mAddress;
    private Double mLat;
    private Double mLng;
    private int mRating;
    private String mUrlPicture;
    private List<Workmate> mWorkmateList;
    private boolean mOpen;
    private String mPhoneNumbers;
    private String mWebsite;


    public Place(String placeId, String name, String address, Double lat, Double lng, int rating,
                 String urlPicture, List<Workmate> workmateList, boolean open, String phoneNumbers, String website) {
        this.placeId = placeId;
        this.mName = name;
        this.mAddress = address;
        this.mLat = lat;
        this.mLng = lng;
        this.mRating = rating;
        this.mUrlPicture = urlPicture;
        this.mWorkmateList = workmateList;
        this.mOpen = open;
        this.mPhoneNumbers = phoneNumbers;
        this.mWebsite = website;
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

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
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

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean open) {
        mOpen = open;
    }

    public String getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
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
