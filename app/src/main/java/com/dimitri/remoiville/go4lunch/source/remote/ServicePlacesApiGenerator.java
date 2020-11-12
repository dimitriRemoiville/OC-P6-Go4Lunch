package com.dimitri.remoiville.go4lunch.source.remote;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicePlacesApiGenerator {

    private static OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .addInterceptor(new OkHttpProfilerInterceptor());

    private static OkHttpClient client = builder.build();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client);

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RequestGoogleApi sRequestGoogleApi = retrofit.create(RequestGoogleApi.class);

    public static RequestGoogleApi getRequestGoogleApi() {return sRequestGoogleApi;
    }
}
