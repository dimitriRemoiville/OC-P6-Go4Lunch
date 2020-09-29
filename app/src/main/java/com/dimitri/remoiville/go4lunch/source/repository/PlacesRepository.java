package com.dimitri.remoiville.go4lunch.source.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.PlacesApiService;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;


public class PlacesRepository {

    private PlacesApiService mApiService;
    private MediatorLiveData<List<PlacesPOJO>> mMediatorLiveData = new MediatorLiveData<>();
    private final String mType = "restaurant";


    public PlacesRepository() {
        PlacesApiService placesApiService = PlacesApiService.retrofit.create(PlacesApiService.class);

    }

    public void getNearbyPlaces(double lat, double lng, int radius, String key) {
        String location = lat + "," + lng;
        final LiveData<List<PlacesPOJO>> source = LiveDataReactiveStreams.fromPublisher(
                mApiService.getNearbyPlaces(location, radius, mType, key)
                .subscribeOn(Schedulers.io())
        );

        mMediatorLiveData.addSource(source, new Observer<List<PlacesPOJO>>() {
            @Override
            public void onChanged(List<PlacesPOJO> placesPOJOS) {
                mMediatorLiveData.setValue(placesPOJOS);
                mMediatorLiveData.removeSource(source);
            }
        });
    }

    public LiveData<List<PlacesPOJO>> observeNearbyPlaces() {
        return mMediatorLiveData;
    }
}
