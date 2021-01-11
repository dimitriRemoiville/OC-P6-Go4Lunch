package com.dimitri.remoiville.go4lunch;

import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.model.User;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class PlaceRestaurantUnitTest {

    private final String mName0 = "The Rose Hotel";
    private final String mName1 = "The Rose Hotel 1";
    private final String mName2 = "The Rose Hotel 2";
    private final String mName3 = "The Rose Hotel 3";
    private final String mName4 = "The Rose Hotel 4";
    private final String mAddress = "52-54 Cleveland St, Chippendale NSW 2008";
    private final Double mLat = -33.8847945;
    private final Double mLng = 151.1950215;
    private final int mRating = (int) Math.round((4.2 * 3) / 5);
    private final String mUrlPicture = "https://www.google.com";
    private final List<User> mUserList = new ArrayList<>();
    private final boolean mOpen = true;
    private final String mPhoneNumbers = "+61293181133";
    private final String mWebsite = "https://www.therosehotel.com.au/";
    private final int mDistance0 = 150;
    private final int mDistance1 = 200;
    private final int mDistance2 = 25;
    private final int mDistance3 = 10;
    private final int mDistance4 = 250;


    @Test
    public void addPlace() {
        PlaceRestaurant placeRestaurant = new PlaceRestaurant(UUID.randomUUID().toString(),mName0, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance0);
        assertEquals(0, placeRestaurant.getUserList().size());
        assertEquals("The Rose Hotel", placeRestaurant.getName());
    }

    @Test
    public void SortPlaceDistance() {
        List<PlaceRestaurant> expected = new ArrayList<>();
        List<PlaceRestaurant> actual = new ArrayList<>();
        PlaceRestaurant placeRestaurant0 = new PlaceRestaurant(UUID.randomUUID().toString(),mName0, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance0);
        PlaceRestaurant placeRestaurant1 = new PlaceRestaurant(UUID.randomUUID().toString(),mName1, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance1);
        PlaceRestaurant placeRestaurant2 = new PlaceRestaurant(UUID.randomUUID().toString(),mName2, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance2);
        PlaceRestaurant placeRestaurant3 = new PlaceRestaurant(UUID.randomUUID().toString(),mName3, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance3);
        PlaceRestaurant placeRestaurant4 = new PlaceRestaurant(UUID.randomUUID().toString(),mName4, mAddress, mLat, mLng, mRating, mUrlPicture, mUserList, mOpen, mPhoneNumbers, mWebsite, mDistance4);

        actual.add(placeRestaurant0); actual.add(placeRestaurant1); actual.add(placeRestaurant2); actual.add(placeRestaurant3); actual.add(placeRestaurant4);
        Collections.sort(actual, new PlaceRestaurant.PlaceDistanceComparator());
        expected.add(placeRestaurant3); expected.add(placeRestaurant2); expected.add(placeRestaurant0); expected.add(placeRestaurant1); expected.add(placeRestaurant4);

        assertEquals(actual.get(0).getName(), expected.get(0).getName());
        assertEquals(actual.get(1).getName(), expected.get(1).getName());
        assertEquals(actual.get(2).getName(), expected.get(2).getName());
        assertEquals(actual.get(3).getName(), expected.get(3).getName());
        assertEquals(actual.get(4).getName(), expected.get(4).getName());
    }
}
