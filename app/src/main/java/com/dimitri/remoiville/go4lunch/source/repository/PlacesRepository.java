package com.dimitri.remoiville.go4lunch.source.repository;

import android.location.Location;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlaceDetailsPOJO;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesRepository {

    private final String mType = "restaurant";
    private final MutableLiveData<List<Place>> listRestaurants = new MutableLiveData<>();

    private static PlacesRepository sPlacesRepository;

    private final String TAG = "PlacesRepository";

    public static PlacesRepository getInstance(){
        if (sPlacesRepository == null) {
            sPlacesRepository = new PlacesRepository();
        }
        return sPlacesRepository;
    }

    public void getListRestaurants(Location location, int radius, String key) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        String latLng = lat + "," + lng;
        Call<PlacesPOJO> listRestaurantsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getNearbyPlaces(latLng, radius, mType, key);
        listRestaurantsPOJOOut.enqueue(new Callback<PlacesPOJO>() {
            @Override
            public void onResponse(Call<PlacesPOJO> call, Response<PlacesPOJO> response) {
                List<Place> list = new ArrayList<>();
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    Place place = new Place(response.body().getResults().get(i), location, key);
                    list.add(place);
                }
/*                if (response.body().getNextPageToken() != null) {
                    getListRestaurantsNext(response.body().getNextPageToken(), location, key);
                }*/
                listRestaurants.setValue(list);
            }

            @Override
            public void onFailure(Call<PlacesPOJO> call, Throwable t) {
                listRestaurants.postValue(null);
            }
        });
    }

    public MutableLiveData<List<Place>> getMutablePlace() {
        return listRestaurants;
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
                            Place place = new Place(response.body().getResults().get(i), location, key);
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

    public Place getRestaurantDetails(String placeId, String key) {
        final Place[] restaurantDetail = new Place[1];
        Call<PlaceDetailsPOJO> restaurantDetailsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getPlaceDetails(placeId, key);
        restaurantDetailsPOJOOut.enqueue(new Callback<PlaceDetailsPOJO>() {
            @Override
            public void onResponse(Call<PlaceDetailsPOJO> call, Response<PlaceDetailsPOJO> response) {
                restaurantDetail[0] = new Place(response.body().getResult(), key);
            }

            @Override
            public void onFailure(Call<PlaceDetailsPOJO> call, Throwable t) {
                restaurantDetail[0] = null;
            }
        });
        return restaurantDetail[0];
    }
}
