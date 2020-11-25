package com.dimitri.remoiville.go4lunch.viewmodel;


import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.RestaurantFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private final RestaurantFirestoreRepository mRestaurantFirestoreRepository;
    private final UserFirestoreRepository mUserFirestoreRepository;

    private MutableLiveData<List<Place>> listRestaurants = new MutableLiveData<>();
    private MutableLiveData<Place> restaurantDetails = new MutableLiveData<>();

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    private MutableLiveData<Place> restaurantFirestore = new MutableLiveData<>();
    private MutableLiveData<List<Place>> listRestaurantsFirestore = new MutableLiveData<>();

    private static final String TAG = "MainViewModel";

    // Constructor
    public MainViewModel(PlacesRepository placesRepository, RestaurantFirestoreRepository restaurantFirestoreRepository, UserFirestoreRepository userFirestoreRepository) {
        this.mPlacesRepository = placesRepository;
        this.mRestaurantFirestoreRepository = restaurantFirestoreRepository;
        this.mUserFirestoreRepository = userFirestoreRepository;
    }

    // API Places
    public MutableLiveData<List<Place>> getRestaurantsRepository() {
        return listRestaurants;
    }

    public void setRestaurantsData(Location location, int radius, String key) {
        listRestaurants.setValue(mPlacesRepository.getListRestaurants(location, radius, key));
    }

    public MutableLiveData<Place> getRestaurantDetailsRepository(String placeId, String key) {
        return restaurantDetails;
    }

    public void setRestaurantDetailsData(String placeId, String key) {
        restaurantDetails.setValue(mPlacesRepository.getRestaurantDetails(placeId,key));
    }

    // Cloud Firestore
    public MutableLiveData<User> getCurrentUser(String userID) {
        mUserFirestoreRepository.getUser(userID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    currentUser.setValue(user);
                }


            }
        });
        return currentUser;
    }

}
