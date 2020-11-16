package com.dimitri.remoiville.go4lunch.source.repository;

import androidx.lifecycle.MutableLiveData;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesRepository {

    private final String mType = "restaurant";
    private final String mUnit = "metric";
    private final String mMode = "walking";
    private final String mFields = "international_phone_number,website";

    private MutableLiveData<List<Place>> listRestaurantsMutableLiveData = new MutableLiveData<>();
    private List<Place> listRestaurants = new ArrayList<>();

    private final String TAG = "PlacesRepository";

    private static PlacesRepository sPlacesRepository;

    public static PlacesRepository getInstance(){
        if (sPlacesRepository == null) {
            sPlacesRepository = new PlacesRepository();
        }
        return sPlacesRepository;
    }

    public MutableLiveData<List<Place>> getListRestaurants(Double lat, Double lng, int radius, String key) {
        String location = lat + "," + lng;
        Call<PlacesPOJO> listRestaurantsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getNearbyPlaces(location, radius, mType, key);
        listRestaurantsPOJOOut.enqueue(new Callback<PlacesPOJO>() {
            @Override
            public void onResponse(Call<PlacesPOJO> call, Response<PlacesPOJO> response) {
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    Place place = new Place(response.body().getResults().get(i), key);
                    listRestaurants.add(place);
                }
                listRestaurantsMutableLiveData.setValue(listRestaurants);
            }

            @Override
            public void onFailure(Call<PlacesPOJO> call, Throwable t) {
                listRestaurantsMutableLiveData.postValue(null);
            }
        });
        return listRestaurantsMutableLiveData;
    }


/*
    public Observable<PlacesPOJO> streamFetchPlacesRestaurants(String location, int radius, String key) {
        return ServicePlacesApiGenerator.getRequestGoogleApi().getNearbyPlaces(location, radius, mType, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public Observable<List<Place>> streamFetchListPlacesRestaurants(Double lat, Double lng, int radius, String key) {
        String location = lat + "," + lng;
        return streamFetchPlacesRestaurants(location, radius, key)
                .map(placesPOJO -> {
                    List<Place> restaurants = new ArrayList<>();

                    List<PlacesPOJO.Result> resultsList = placesPOJO.getResults();
                    Log.d(TAG, "streamFetchListPlacesRestaurants: " + resultsList.size());

                    for (int i = 0; i < resultsList.size(); i++) {
                        String placeId = "";
                        if (resultsList.get(i).getPlaceId() != null) {
                            placeId = resultsList.get(i).getPlaceId();
                        }

                        String name = "";
                        if (resultsList.get(i).getName() != null) {
                            name = resultsList.get(i).getName();
                        }

                        String address = "";
                        if (resultsList.get(i).getVicinity() != null) {
                            address = resultsList.get(i).getVicinity();
                        }

                        String photo = "";
                        if (resultsList.get(i).getPhotos() != null) {
                            photo = getPlacesPhoto(resultsList.get(i).getPhotos().get(0).getPhotoReference(), key);
                        }

                        int rating = 0;
                        if (resultsList.get(i).getRating() != null) {
                            rating = (int) Math.round((resultsList.get(i).getRating() * 3) / 5);
                        }
                        boolean openNow = false;
                        if (resultsList.get(i).getOpeningHours() != null) {
                            openNow = resultsList.get(i).getOpeningHours().getOpenNow();
                        }
                        List<Workmate> workmateList = new ArrayList<>();

                        if (resultsList.get(i).getGeometry() != null) {
                            Place place = new Place(placeId, name, address, resultsList.get(i).getGeometry().getLocation().getLat(), resultsList.get(i).getGeometry().getLocation().getLng(),
                                    rating, photo, workmateList, openNow, "", "");
                            restaurants.add(place);
                        }
                        Log.d(TAG, "streamFetchListPlacesRestaurants: " + i + " /" + resultsList.get(i).getName());
                    }
                    return restaurants;
                });
    }
*/

/*    public Observable<DistanceMatrix> streamFetchDistanceMatrix(String location, String destinations, String key) {
        return ServicePlacesApiGenerator.getRequestGoogleApi().getDistance(mUnit, location, destinations, mMode, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public Observable<List<Integer>> streamFetchListDistance(Double lat, Double lng, String destinations, String key) {
        String location = lat + "," + lng;
        return streamFetchDistanceMatrix(location, destinations, key)
                .map(distanceMatrix -> {

                    List<Integer> distanceList = new ArrayList<>();
                    List<Element> resultList = distanceMatrix.getRows().get(0).getElements();

                    for (int i = 0; i < resultList.size(); i++) {
                        distanceList.add(resultList.get(i).getDistance().getValue());
                    }

                    return distanceList;
                });
    }

    public Flowable<PlacesPOJO> streamFetchPlaceDetails(String placeId, String key) {
        return ServicePlacesApiGenerator.getRequestGoogleApi()
                .getPlaceDetails(placeId, mFields, key)
                .subscribeOn(Schedulers.io());
    }*/
}
