package com.dimitri.remoiville.go4lunch.viewmodel;


import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private MutableLiveData<List<Place>> listRestaurants = new MutableLiveData<>();
    private MutableLiveData<Place> restaurantDetails = new MutableLiveData<>();
    private static final String TAG = "MainViewModel";

    public MainViewModel(PlacesRepository placesRepository) {
        this.mPlacesRepository = placesRepository;
    }

    public MutableLiveData<List<Place>> getRestaurantsRepository(Location location, int radius, String key) {
        return listRestaurants = loadRestaurantsData(location, radius, key);
    }

    private  MutableLiveData<List<Place>> loadRestaurantsData(Location location, int radius, String key) {
        return mPlacesRepository.getListRestaurants(location, radius, key);
    }

    public MutableLiveData<Place> getRestaurantDetailsRepository(String placeId, String key) {
        return restaurantDetails = loadRestaurantDetailsData(placeId, key);
    }

    private MutableLiveData<Place> loadRestaurantDetailsData(String placeId, String key) {
        return mPlacesRepository.getRestaurantDetails(placeId,key);
    }

/*    public MutableLiveData<Observable<List<Place>>> streamFetchPlacesRestaurants(Double lat, Double lng, int radius, String key) {

        Log.d(TAG, "streamFetchPlacesRestaurants: ici");
        mListRestaurantsMutable.setValue(mPlacesRepository.streamFetchListPlacesRestaurants(lat,lng,radius,key));
        return mListRestaurantsMutable;
    }

    public MutableLiveData<Observable<List<Integer>>> streamFetchDistances(Double lat, Double lng, String destinations, String key) {
        mListDistanceMutable.setValue(mPlacesRepository.streamFetchListDistance(lat,lng,destinations,key));
        return mListDistanceMutable;
    }*/
}
