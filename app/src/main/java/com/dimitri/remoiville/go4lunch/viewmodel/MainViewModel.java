package com.dimitri.remoiville.go4lunch.viewmodel;


import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private final UserFirestoreRepository mUserFirestoreRepository;

    private MutableLiveData<List<Place>> listRestaurants = new MutableLiveData<>();
    private MutableLiveData<Place> restaurantDetails = new MutableLiveData<>();

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private List<User> userList = new ArrayList<>();

    private MutableLiveData<Place> restaurantFirestore = new MutableLiveData<>();
    private MutableLiveData<List<Place>> listRestaurantsFirestore = new MutableLiveData<>();

    private static final String TAG = "MainViewModel";

    // Constructor
    public MainViewModel(PlacesRepository placesRepository, UserFirestoreRepository userFirestoreRepository) {
        this.mPlacesRepository = placesRepository;
        this.mUserFirestoreRepository = userFirestoreRepository;
    }

    // API Places
    public MutableLiveData<List<Place>> getRestaurantsData(Location location, int radius, String key) {
        return listRestaurants = mPlacesRepository.getListRestaurants(location, radius, key);
    }

    public MutableLiveData<Place> getRestaurantDetailsData(String placeId, String key) {
        return restaurantDetails = mPlacesRepository.getRestaurantDetails(placeId,key);
    }

    // Cloud Firestore
    // Get the current user
    public MutableLiveData<User> getCurrentUser(String userID) {
        mUserFirestoreRepository.getUser(userID).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                currentUser.setValue(user);
            }


        });
        return currentUser;
    }

    // Get all users
    public MutableLiveData<List<User>> getAllUsers() {
        mUserFirestoreRepository.getAllUsers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (int i = 0; i < documents.size(); i++) {
                        User user = documents.get(i).toObject(User.class);
                        userList.add(user);
                    }
                    usersMutableLiveData.setValue(userList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return usersMutableLiveData;
    }

    // Create new user
    public void createNewUser(String userID, String firstName, String lastName, String email) {
        mUserFirestoreRepository.createUser(userID, firstName, lastName, email);
    }
}
