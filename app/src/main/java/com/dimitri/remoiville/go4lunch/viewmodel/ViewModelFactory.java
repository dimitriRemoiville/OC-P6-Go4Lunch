package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dimitri.remoiville.go4lunch.source.repository.MessageFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PlacesRepository mPlacesRepository;
    private final UserFirestoreRepository mUserFirestoreRepository;
    private final MessageFirestoreRepository mMessageFirestoreRepository;

    public ViewModelFactory(PlacesRepository placesRepository, UserFirestoreRepository userFirestoreRepository, MessageFirestoreRepository messageFirestoreRepository) {
        this.mPlacesRepository = placesRepository;
        this.mUserFirestoreRepository = userFirestoreRepository;
        this.mMessageFirestoreRepository = messageFirestoreRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mPlacesRepository, mUserFirestoreRepository, mMessageFirestoreRepository);
        }
        throw new IllegalArgumentException("Unknown MainViewModel Class");
    }
}
