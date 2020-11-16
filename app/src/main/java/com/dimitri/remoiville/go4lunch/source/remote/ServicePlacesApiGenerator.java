package com.dimitri.remoiville.go4lunch.source.remote;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicePlacesApiGenerator {

    private static final OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .addInterceptor(new OkHttpProfilerInterceptor());

    private static final OkHttpClient client = builder.build();

    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client);

    private static final Retrofit retrofit = retrofitBuilder.build();

    private static final RequestGoogleApi sRequestGoogleApi = retrofit.create(RequestGoogleApi.class);

    public static RequestGoogleApi getRequestGoogleApi() {
        return sRequestGoogleApi;
    }
}
