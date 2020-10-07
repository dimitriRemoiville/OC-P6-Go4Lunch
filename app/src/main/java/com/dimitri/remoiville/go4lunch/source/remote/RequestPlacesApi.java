package com.dimitri.remoiville.go4lunch.source.remote;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RequestPlacesApi {


    @GET("nearbysearch/json?")
    Flowable<PlacesPOJO> getNearbyPlaces (@Query("location") String location,
                                            @Query("radius") int radius,
                                            @Query("type") String type,
                                            @Query("key") String key);

    @GET("nearbysearch/json?")
    Flowable<PlacesPOJO> getNearbyPlacesNext (@Query("pagetoken") String pageToken,
                                                @Query("key") String key);

    @GET("details/json?")
    Flowable<PlacesPOJO> getPlaceDetails (@Query("place_id") String placesId,
                                              @Query("fields") String fields,
                                              @Query("key") String key);

    @GET("photos?")
    String getPlacePhoto (@Query("photoreference") String photoReference,
                                              @Query("maxWidth") int maxWidth,
                                              @Query("key") String key);


}
