package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private static final String TAG = "MainViewModel";
    private MutableLiveData<Observable<List<PlacesPOJO>>> listPlacesRestaurants = new MutableLiveData<>();

    public MainViewModel(PlacesRepository placesRepository) {
        super();
        mPlacesRepository = placesRepository;
    }

    public LiveData<List<PlacesPOJO>> streamFetchPlacesRestaurants(String location, int radius, String key) {
        return mPlacesRepository.streamFetchPlacesRestaurants(location,radius,key);
    }
}
