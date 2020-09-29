package com.dimitri.remoiville.go4lunch.source.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;

import java.util.List;

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
        return LiveDataReactiveStreams.fromPublisher(ServicePlacesApiGenerator.getRequestPlacesApi()
                        .getNearbyPlaces(location,radius,mType,key)
                        .subscribeOn(Schedulers.io()));
    }

/*    public Observable<List<PlacesPOJO>> streamFetchPlacesRestaurants(String location, int radius, String key) {
        RequestPlacesApi placesApiService = RequestPlacesApi.retrofit.create(RequestPlacesApi.class);
        Log.d(TAG, "streamFetchPlacesRestaurants: ici");
        return placesApiService.getNearbyPlaces(location, radius, mType, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }*/
}
