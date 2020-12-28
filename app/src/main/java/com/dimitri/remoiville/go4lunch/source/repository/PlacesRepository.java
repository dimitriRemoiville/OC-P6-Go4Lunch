package com.dimitri.remoiville.go4lunch.source.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.model.PlaceDetailsPOJO;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesRepository {

    private final MutableLiveData<List<PlaceRestaurant>> listRestaurantsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<PlaceRestaurant> restaurantDetail = new MutableLiveData<>();
    private final List<PlaceRestaurant> listRestaurants = new ArrayList<>();

    private static PlacesRepository sPlacesRepository;

    public static PlacesRepository getInstance(){
        if (sPlacesRepository == null) {
            sPlacesRepository = new PlacesRepository();
        }
        return sPlacesRepository;
    }

    public MutableLiveData<List<PlaceRestaurant>> getListRestaurants(Location location, int radius, String key) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String latLng = lat + "," + lng;
        String type = "restaurant";
        Call<PlacesPOJO> listRestaurantsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getNearbyPlaces(latLng, radius, type, key);
        listRestaurantsPOJOOut.enqueue(new Callback<PlacesPOJO>() {
            @Override
            public void onResponse(@NonNull Call<PlacesPOJO> call, @NonNull Response<PlacesPOJO> response) {
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    PlaceRestaurant place = new PlaceRestaurant(response.body().getResults().get(i), location, key);
                    listRestaurants.add(place);
                }
/*                if (response.body().getNextPageToken() != null) {
                    getListRestaurantsNext(response.body().getNextPageToken(), location, key);
                }*/
                listRestaurantsMutableLiveData.setValue(listRestaurants);
            }

            @Override
            public void onFailure(@NonNull Call<PlacesPOJO> call, Throwable t) {
                listRestaurantsMutableLiveData.postValue(null);
            }
        });
        return listRestaurantsMutableLiveData;
    }

/*    private void getListRestaurantsNext(String token, Location location, String key) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<PlacesPOJO> listRestaurantsPOJOOutNext = ServicePlacesApiGenerator.getRequestGoogleApi().getNearbyPlacesNext(token, key);
                listRestaurantsPOJOOutNext.enqueue(new Callback<PlacesPOJO>() {
                    @Override
                    public void onResponse(Call<PlacesPOJO> call, Response<PlacesPOJO> response) {
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            PlaceRestaurant place = new PlaceRestaurant(response.body().getResults().get(i), location, key);
                            listRestaurants.add(place);
                        }
                        if (response.body().getNextPageToken() != null) {
                            getListRestaurantsNext(response.body().getNextPageToken(), location, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesPOJO> call, Throwable t) {

                    }
                });
            }
        }, 2000);
    }*/

    public MutableLiveData<PlaceRestaurant> getRestaurantDetails(String placeId, String key) {
        Call<PlaceDetailsPOJO> restaurantDetailsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getPlaceDetails(placeId,key);
        restaurantDetailsPOJOOut.enqueue(new Callback<PlaceDetailsPOJO>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsPOJO> call, @NonNull Response<PlaceDetailsPOJO> response) {
                PlaceRestaurant place = new PlaceRestaurant(response.body().getResult(), key);
                restaurantDetail.setValue(place);
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsPOJO> call, @NonNull Throwable t) {
                restaurantDetail.postValue(null);
            }
        });
        return restaurantDetail;
    }
}
