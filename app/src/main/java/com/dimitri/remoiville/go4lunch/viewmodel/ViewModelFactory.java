package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.RestaurantFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PlacesRepository mPlacesRepository;
    private final RestaurantFirestoreRepository mRestaurantFirestoreRepository;
    private final UserFirestoreRepository mUserFirestoreRepository;

    public ViewModelFactory(PlacesRepository placesRepository, RestaurantFirestoreRepository restaurantFirestoreRepository, UserFirestoreRepository userFirestoreRepository) {
        this.mPlacesRepository = placesRepository;
        this.mRestaurantFirestoreRepository = restaurantFirestoreRepository;
        this.mUserFirestoreRepository = userFirestoreRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mPlacesRepository, mRestaurantFirestoreRepository, mUserFirestoreRepository);
        }
        throw new IllegalArgumentException("Unknown MainViewModel Class");
    }
}
