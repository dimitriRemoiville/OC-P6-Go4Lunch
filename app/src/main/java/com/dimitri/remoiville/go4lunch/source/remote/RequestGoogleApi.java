package com.dimitri.remoiville.go4lunch.source.remote;

import com.dimitri.remoiville.go4lunch.model.DistanceMatrix;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RequestGoogleApi {


    @GET("place/nearbysearch/json?")
    Observable<PlacesPOJO> getNearbyPlaces (@Query("location") String location,
                                            @Query("radius") int radius,
                                            @Query("type") String type,
                                            @Query("key") String key);

    @GET("distancematrix/json?")
    Observable<DistanceMatrix> getDistance (@Query("units") String units,
                                            @Query("origins") String location,
                                            @Query("destinations") String destinations,
                                            @Query("mode") String mode,
                                            @Query("key") String key);

    @GET("nearbysearch/json?")
    Flowable<PlacesPOJO> getNearbyPlacesNext (@Query("pagetoken") String pageToken,
                                                @Query("key") String key);

    @GET("details/json?")
    Flowable<PlacesPOJO> getPlaceDetails (@Query("place_id") String placesId,
                                              @Query("fields") String fields,
                                              @Query("key") String key);

}
