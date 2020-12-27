package com.dimitri.remoiville.go4lunch.viewmodel;


import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.source.repository.MessageFirestoreRepository;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainViewModel extends ViewModel {

    private final PlacesRepository mPlacesRepository;
    private final UserFirestoreRepository mUserFirestoreRepository;
    private final MessageFirestoreRepository mMessageFirestoreRepository;

    private MutableLiveData<List<PlaceRestaurant>> listRestaurants = new MutableLiveData<>();
    private MutableLiveData<PlaceRestaurant> restaurantDetails = new MutableLiveData<>();

    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private final List<User> users = new ArrayList<>();

    private static final String TAG = "MainViewModel";


    // Constructor
    public MainViewModel(PlacesRepository placesRepository, UserFirestoreRepository userFirestoreRepository, MessageFirestoreRepository messageFirestoreRepository) {
        this.mPlacesRepository = placesRepository;
        this.mUserFirestoreRepository = userFirestoreRepository;
        this.mMessageFirestoreRepository = messageFirestoreRepository;
    }

    // API Places
    public MutableLiveData<List<PlaceRestaurant>> getRestaurantsData(Location location, int radius, String key) {
        return listRestaurants = mPlacesRepository.getListRestaurants(location, radius, key);
    }

    public MutableLiveData<PlaceRestaurant> getRestaurantDetailsData(String placeId, String key) {
        return restaurantDetails = mPlacesRepository.getRestaurantDetails(placeId,key);
    }

    // Cloud Firestore
    // Get the current user
    public MutableLiveData<User> getCurrentUser(String userID) {
        mUserFirestoreRepository.getUser(userID).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                currentUser.setValue(user);
            } else {
                currentUser.setValue(null);
            }


        })
                .addOnFailureListener(e -> currentUser.setValue(null));
        return currentUser;
    }

    // Create new user
    public void createNewUser(User user) {
        mUserFirestoreRepository.createUser(user);
    }

    // Get all users sort by restaurant ID
    public MutableLiveData<List<User>> getAllUsersSortByRestaurantID() {
        mUserFirestoreRepository.getAllUsersSortByRestaurantID().addOnCompleteListener(this::createUsersList);
        return usersMutableLiveData;
    }

    // Get users with a placeID not null
    public MutableLiveData<List<User>> getUsersPlaceIDNotNull() {
        mUserFirestoreRepository.getUsersPlaceIDNotNull().addOnCompleteListener(this::createUsersList);
        return usersMutableLiveData;
    }

    // Get users eating at a specific place
    public MutableLiveData<List<User>> getUsersWithPlaceID(String placeID) {
        mUserFirestoreRepository.getUsersWithPlaceID(placeID).addOnCompleteListener(this::createUsersList);
        return usersMutableLiveData;
    }

    private void createUsersList(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            users.clear();
            for (int i = 0; i < documents.size(); i++) {
                User user = documents.get(i).toObject(User.class);
                users.add(user);
            }
            usersMutableLiveData.setValue(users);
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }

    // Update user name, email and/or profile picture in Firestore
    public void updateUserData(String userID, String firstName, String lastName, String email, String urlProfilePicture) {
        mUserFirestoreRepository.updateUserData(userID, firstName, lastName, email, urlProfilePicture);
    }

    // Update in the user collections where the user wants to go for the lunch
    public void updateLunchID(String userID, String placeID, String restaurantName) {
        mUserFirestoreRepository.updateLunchID(userID, placeID, restaurantName);
    }

    // Update the list of restaurant liked by the use
    public void updateLikesList(String userID, List<String> likesList) {
        mUserFirestoreRepository.updateLikesList(userID, likesList);
    }

    // Update the boolean who signal if a user subscribe to the notifications services
    public void updateHasChosenNotification(String userID, boolean hasChosenNotification) {
        mUserFirestoreRepository.updateHasChosenNotification(userID, hasChosenNotification);
    }

    // Delete the specified user
    public Task<Void> deleteUser(String userID) {
        return mUserFirestoreRepository.deleteUser(userID);
    }
}
