package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PlacesRepository mPlacesRepository;

    public ViewModelFactory(PlacesRepository placesRepository) {
        this.mPlacesRepository = placesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mPlacesRepository);
        }
        throw new IllegalArgumentException("Unknown MainViewModel Class");
    }
}
