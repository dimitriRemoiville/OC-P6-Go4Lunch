package com.dimitri.remoiville.go4lunch.source.remote;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RequestPlacesApi {


    @GET("nearbysearch/json?")
    Flowable<List<PlacesPOJO>> getNearbyPlaces (@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String key);
}
