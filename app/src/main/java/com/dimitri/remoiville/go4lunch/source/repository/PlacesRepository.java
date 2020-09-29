package com.dimitri.remoiville.go4lunch.source.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.PlacesApiService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlacesRepository {

    private static final String mType = "restaurant";
    private static final String TAG = "PlacesRepository";
    private static PlacesRepository instance;


    public static PlacesRepository getInstance() {
        if(instance == null) {
            instance = new PlacesRepository();
        }
        return instance;
    }


    public LiveData<List<PlacesPOJO>> streamFetchPlacesRestaurants(String location, int radius, String key) {
        PlacesApiService placesApiService = PlacesApiService.retrofit.create(PlacesApiService.class);
        return LiveDataReactiveStreams.fromPublisher(placesApiService
                        .getNearbyPlaces(location,radius,mType,key)
                        .subscribeOn(Schedulers.io()));
    }

/*    public Observable<List<PlacesPOJO>> streamFetchPlacesRestaurants(String location, int radius, String key) {
        PlacesApiService placesApiService = PlacesApiService.retrofit.create(PlacesApiService.class);
        Log.d(TAG, "streamFetchPlacesRestaurants: ici");
        return placesApiService.getNearbyPlaces(location, radius, mType, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }*/
}
