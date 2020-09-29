package com.dimitri.remoiville.go4lunch.utils;

import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

public class Injection {


    public static PlacesRepository providePlacesRepository() {
        return new PlacesRepository();
    }


    public static ViewModelFactory provideViewModelFactory() {
        PlacesRepository placesRepository = providePlacesRepository();
        return new ViewModelFactory(placesRepository);
    }
}
