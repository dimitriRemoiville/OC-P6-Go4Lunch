package com.dimitri.remoiville.go4lunch.model;

import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
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
    private List<User> mUserList;
    private boolean mOpen;
    private String mPhoneNumbers;
    private String mWebsite;
    private int mDistance;
    private String mIcon;

    public Place(String placeId, String name, String address, Double lat, Double lng, int rating, String urlPicture, List<User> userList, boolean open, String phoneNumbers, String website, int distance, String icon) {
        this.placeId = placeId;
        mName = name;
        mAddress = address;
        mLat = lat;
        mLng = lng;
        mRating = rating;
        mUrlPicture = urlPicture;
        mUserList = userList;
        mOpen = open;
        mPhoneNumbers = phoneNumbers;
        mWebsite = website;
        mDistance = distance;
        mIcon = icon;
    }

    public Place(@NonNull PlacesPOJO.Result POJOResult, Location location, String key) {
        placeId = "";
        if (POJOResult.getPlaceId() != null) {
            placeId = POJOResult.getPlaceId();
        }

        mName = "";
        if (POJOResult.getName() != null) {
            mName = POJOResult.getName();
        }

        mAddress = "";
        if (POJOResult.getVicinity() != null) {
            mAddress = POJOResult.getVicinity();
        }

        mLat = 0.0;
        mLng = 0.0;
        mDistance = 0;
        if (POJOResult.getGeometry() != null) {
            mLat = POJOResult.getGeometry().getLocation().getLat();
            mLng = POJOResult.getGeometry().getLocation().getLng();

            Location locationDestination = new Location(LocationManager.GPS_PROVIDER);
            locationDestination.setLatitude(mLat);
            locationDestination.setLongitude(mLng);
            mDistance = (int) location.distanceTo(locationDestination);
        }

        mRating = 0;
        if (POJOResult.getRating() != null) {
            mRating = (int) Math.round((POJOResult.getRating() * 3) / 5);
        }

        mUrlPicture = "";
        if (POJOResult.getPhotos() != null) {
            mUrlPicture = getPlacesPhoto(POJOResult.getPhotos().get(0).getPhotoReference(), key);
        }

        mUserList = new ArrayList<>();

        mOpen = false;
        if (POJOResult.getOpeningHours() != null) {
            mOpen = POJOResult.getOpeningHours().getOpenNow();
        }

        mPhoneNumbers = "";
        mWebsite = "";

        mIcon = "";
        if (POJOResult.getIcon() != null) {
            mIcon = POJOResult.getIcon();
        }
    }

    public Place(PlaceDetailsPOJO.Result detailsPOJO , String key) {
        // name,formatted_address,international_phone_number,website,photos
        placeId = "";

        mName = "";
        if (detailsPOJO.getName() != null) {
            mName = detailsPOJO.getName();
        }

        mAddress = "";
        if (detailsPOJO.getFormattedAddress() != null) {
            mAddress = detailsPOJO.getFormattedAddress();
        }

        mLat = 0.0;
        mLng = 0.0;
        mDistance = 0;

        mRating = 0;
        if (detailsPOJO.getRating() != null) {
            mRating = (int) Math.round((detailsPOJO.getRating() * 3) / 5);
        }

        mUrlPicture = "";
        if (detailsPOJO.getPhotos() != null) {
            mUrlPicture = getPlacesPhoto(detailsPOJO.getPhotos().get(0).getPhotoReference(), key);
        }

        mUserList = new ArrayList<>();
        mOpen = false;

        mPhoneNumbers = "";
        if (detailsPOJO.getInternationalPhoneNumber() != null) {
            mPhoneNumbers = detailsPOJO.getInternationalPhoneNumber();
        }

        mWebsite = "";
        if (detailsPOJO.getWebsite() != null) {
            mWebsite = detailsPOJO.getWebsite();
        }

        mIcon = "";
    }

    private String getPlacesPhoto(String photoReference, String key) {
        int maxWidth = 400;
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth + "&photoreference=" + photoReference + "&key=" + key;
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

    public List<User> getUserList() {
        return mUserList;
    }

    public void setUserList(List<User> userList) {
        mUserList = userList;
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

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
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

    public static class PlaceDistanceComparator implements Comparator<Place> {
        @Override
        public int compare(Place left, Place right) {
            return (int) (left.mDistance - right.mDistance);
        }
    }
}


