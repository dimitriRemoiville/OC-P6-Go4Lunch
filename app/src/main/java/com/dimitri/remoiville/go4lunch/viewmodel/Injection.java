package com.dimitri.remoiville.go4lunch.viewmodel;

import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.RestaurantFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;

public class Injection {


    public static PlacesRepository providePlacesRepository() {
        return new PlacesRepository();
    }

    public static RestaurantFirestoreRepository provideRestaurantFirestoreRepository() {
        return new RestaurantFirestoreRepository();
    }

    public static UserFirestoreRepository provideUserFirestoreRepository() {
        return new UserFirestoreRepository();
    }


    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(providePlacesRepository(),
                provideRestaurantFirestoreRepository(),
                provideUserFirestoreRepository());
    }
}
