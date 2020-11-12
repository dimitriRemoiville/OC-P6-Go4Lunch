package com.dimitri.remoiville.go4lunch.viewmodel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;

import io.reactivex.Observable;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private MutableLiveData<Observable<List<Place>>> mListRestaurantsMutable = new MutableLiveData<>();
    private MutableLiveData<Observable<List<Integer>>> mListDistanceMutable = new MutableLiveData<>();
    private static final String TAG = "MainViewModel";

    public MainViewModel(PlacesRepository placesRepository) {
        this.mPlacesRepository = placesRepository;
    }

    public MutableLiveData<Observable<List<Place>>> streamFetchPlacesRestaurants(Double lat, Double lng, int radius, String key) {

        Log.d(TAG, "streamFetchPlacesRestaurants: ici");
        mListRestaurantsMutable.setValue(mPlacesRepository.streamFetchListPlacesRestaurants(lat,lng,radius,key));
        return mListRestaurantsMutable;
    }

    public MutableLiveData<Observable<List<Integer>>> streamFetchDistances(Double lat, Double lng, String destinations, String key) {
        mListDistanceMutable.setValue(mPlacesRepository.streamFetchListDistance(lat,lng,destinations,key));
        return mListDistanceMutable;
    }
}
