package com.dimitri.remoiville.go4lunch.source.repository;

import android.util.Log;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.model.Result;
import com.dimitri.remoiville.go4lunch.model.Workmate;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class PlacesRepository {

    private static final String mType = "restaurant";
    private static final int mMaxWidth = 400;
    private static final String mFields = "international_phone_number,website";
    private static PlacesRepository instance;
    private static final String TAG = "PlacesRepository";


    public static PlacesRepository getInstance() {
        if(instance == null) {
            instance = new PlacesRepository();
        }
        return instance;
    }

    public Flowable<PlacesPOJO> streamFetchPlacesRestaurants(String location, int radius, String key) {
        return ServicePlacesApiGenerator.getRequestPlacesApi().getNearbyPlaces(location, radius, mType, key);
    }


    public Flowable<List<Place>> streamFetchListPlacesRestaurants(String location, int radius, String key) {
        return streamFetchPlacesRestaurants(location, radius, key)
                .map(new Function<PlacesPOJO, List<Place>>() {
                    @Override
                    public List<Place> apply(PlacesPOJO placesPOJO) {
                        List<Place> restaurantsList = new ArrayList<>();
                        Log.d(TAG, "apply: ici");

                        assert placesPOJO.getResults() != null;
                        List<Result> resultsList = new ArrayList<>(placesPOJO.getResults());

                        for (Result list : resultsList) {
                            String placeId = list.getPlaceId();
                            String name = list.getName();
                            String address = list.getVicinity();
                            Double lat = 0.0, lng = 0.0;
                            Log.d(TAG, "apply: avant location");
                            if (list.getGeometry() != null) {
                                Log.d(TAG, "apply: location" + list.getGeometry().toString());
                                if (list.getGeometry().getLocation() != null) {
                                    Log.d(TAG, "apply: location : " + list.getGeometry().getLocation().getLat() + " / " + name);
                                } else {
                                    Log.d(TAG, "apply: location null / " + name);
                                }
                                //lat = list.getGeometry().getLocation().getLat();
                                //lng = list.getGeometry().getLocation().getLng();
                            }
                            int rating = (int)Math.round((list.getRating()*3)/5);
                            String urlPicture = "";
/*                            if (list.getPhotos() != null) {
                                Log.d(TAG, "apply: photo");
                                urlPicture = getPlacesPhoto(list.getPhotos().get(0).getPhotoReference(), key);
                            }*/
                            List<Workmate> workmateList = new ArrayList<>();
                            boolean openNow = false;
/*                            if (list.getOpeningHours() != null) {
                                Log.d(TAG, "apply: openNow");
                                openNow = list.getOpeningHours().isOpenNow();
                            }*/
                            Place place = new Place(placeId, name, address, lat, lng, rating, urlPicture, workmateList,openNow, "", "");
                            restaurantsList.add(place);
                        }
                        return restaurantsList;
                    }
                });
    }

    public String getPlacesPhoto(String photoReference, String key) {
        return ServicePlacesApiGenerator.getRequestPlacesApi().getPlacePhoto(photoReference, mMaxWidth, key);
    }

    public Flowable<PlacesPOJO> streamFetchPlaceDetails(String placeId, String key) {
        return ServicePlacesApiGenerator.getRequestPlacesApi()
                .getPlaceDetails(placeId,mFields,key)
                .subscribeOn(Schedulers.io());
    }
}
