package com.dimitri.remoiville.go4lunch.source.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicePlacesApiGenerator {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RequestPlacesApi sRequestPlacesApi = retrofit.create(RequestPlacesApi.class);

    public static RequestPlacesApi getRequestPlacesApi() {
        return sRequestPlacesApi;
    }
}
