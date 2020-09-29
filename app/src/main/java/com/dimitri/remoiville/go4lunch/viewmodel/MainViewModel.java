package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;

    public MainViewModel(PlacesRepository placesRepository) {
        super();
        mPlacesRepository = placesRepository;
    }


}
