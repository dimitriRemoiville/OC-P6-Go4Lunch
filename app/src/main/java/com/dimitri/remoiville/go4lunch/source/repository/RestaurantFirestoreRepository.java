package com.dimitri.remoiville.go4lunch.source.repository;

import com.dimitri.remoiville.go4lunch.model.RestaurantFirestore;
import com.dimitri.remoiville.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RestaurantFirestoreRepository {

    // Collection reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("restaurants");
    }

    // Create
    public Task<Void> createRestaurant(String placeID, String name, List<User> workmates) {
        RestaurantFirestore restaurant = new RestaurantFirestore(placeID,name, workmates);
        return getUsersCollection().document(placeID).set(restaurant);
    }

    // Get
    public Task<DocumentSnapshot> getRestaurant(String placeID) {
        return getUsersCollection().document(placeID).get();
    }

    // Update
    public Task<Void> updateName(String placeID, String name) {
        return getUsersCollection().document(placeID).update("name", name);
    }

    public Task<Void> updateListWorkmates(String placeID, List<User> workmates) {
        return getUsersCollection().document(placeID).update("listWorkmates", workmates);
    }

    // Delete
    public Task<Void> deleteRestaurant(String placeID) {
        return getUsersCollection().document(placeID).delete();
    }
}
