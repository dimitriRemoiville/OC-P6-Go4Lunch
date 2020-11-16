package com.dimitri.remoiville.go4lunch.source.remote;

import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RequestGoogleApi {


    @GET("place/nearbysearch/json?")
    Call<PlacesPOJO> getNearbyPlaces (@Query("location") String location,
                                      @Query("radius") int radius,
                                      @Query("type") String type,
                                      @Query("key") String key);

    @GET("place/nearbysearch/json?")
    Call<PlacesPOJO> getNearbyPlacesNext (@Query("pagetoken") String pageToken,
                                                @Query("key") String key);

    @GET("place/details/json?")
    Call<PlacesPOJO> getPlaceDetails (@Query("place_id") String placesId,
                                              @Query("fields") String fields,
                                              @Query("key") String key);

}
