package com.dimitri.remoiville.go4lunch.viewmodel;

import com.dimitri.remoiville.go4lunch.source.repository.MessageFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;

public class Injection {


    public static PlacesRepository providePlacesRepository() {
        return new PlacesRepository();
    }


    public static UserFirestoreRepository provideUserFirestoreRepository() {
        return new UserFirestoreRepository();
    }

    public static MessageFirestoreRepository provideMessageFirestoreRepository() {
        return new MessageFirestoreRepository();
    }


    public static ViewModelFactory provideViewModelFactory() {
        return new ViewModelFactory(providePlacesRepository(),
                provideUserFirestoreRepository(),
                provideMessageFirestoreRepository());
    }
}
