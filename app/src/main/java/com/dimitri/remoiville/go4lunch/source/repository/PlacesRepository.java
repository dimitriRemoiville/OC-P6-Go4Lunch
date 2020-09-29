package com.dimitri.remoiville.go4lunch.source.repository;

import androidx.lifecycle.MediatorLiveData;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.PlacesApiService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlacesRepository {

    private MediatorLiveData<List<PlacesPOJO>> mMediatorLiveData = new MediatorLiveData<>();
    private final String mType = "restaurant";


    public PlacesRepository() {
    }


    public Flowable<List<PlacesPOJO>> streamFetchRestaurants(double lat, double lng, int radius, String key) {
        PlacesApiService placesApiService = PlacesApiService.retrofit.create(PlacesApiService.class);
        String location = lat + "," + lng;
        return placesApiService.getNearbyPlaces(location, radius, mType, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
