package com.dimitri.remoiville.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;

import java.util.List;



public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private MediatorLiveData<List<PlacesPOJO>> mMediatorLiveData = new MediatorLiveData<>();

    public MainViewModel(PlacesRepository placesRepository) {
        super();
        mPlacesRepository = placesRepository;
    }

    public void streamFetchRestaurants(double lat, double lng, int radius, String key) {
        final LiveData<List<PlacesPOJO>> source = LiveDataReactiveStreams.fromPublisher(
                mPlacesRepository.streamFetchRestaurants(lat, lng, radius, key)
        );

        mMediatorLiveData.addSource(source, placesPOJOS -> {
            mMediatorLiveData.setValue(placesPOJOS);
        });
    }


    public MediatorLiveData<List<PlacesPOJO>> observeRestaurants () {
        return mMediatorLiveData;
    }
}
