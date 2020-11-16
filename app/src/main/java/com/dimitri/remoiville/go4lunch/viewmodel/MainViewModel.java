package com.dimitri.remoiville.go4lunch.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private MutableLiveData<List<Place>> listRestaurants = new MutableLiveData<>();
    private static final String TAG = "MainViewModel";

    public MainViewModel(PlacesRepository placesRepository) {
        this.mPlacesRepository = placesRepository;
    }

    public MutableLiveData<List<Place>> getRestaurantsRepository(Double lat, Double lng, int radius, String key) {
        listRestaurants = loadRestaurantsData(lat, lng, radius, key);
        return listRestaurants;
    }

    private  MutableLiveData<List<Place>> loadRestaurantsData(Double lat, Double lng, int radius, String key) {
        return mPlacesRepository.getListRestaurants(lat, lng, radius, key);
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
