package com.dimitri.remoiville.go4lunch.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private static final String TAG = "MainViewModel";
    private MutableLiveData<Observable<List<PlacesPOJO>>> listPlacesRestaurants = new MutableLiveData<>();

    public MainViewModel(PlacesRepository placesRepository) {
        super();
        mPlacesRepository = placesRepository;
    }

    public LiveData<List<Place>> streamFetchPlacesRestaurants(String location, int radius, String key) {
        Log.d(TAG, "streamFetchPlacesRestaurants: ici");
        LiveData<List<Place>> test = LiveDataReactiveStreams.fromPublisher(
                mPlacesRepository.streamFetchListPlacesRestaurants(location,radius,key)
                        .subscribeOn(Schedulers.io()));
        Log.d(TAG, "streamFetchPlacesRestaurants: là");
        return test;
    }
}
