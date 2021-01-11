package com.dimitri.remoiville.go4lunch.source.repository;

import com.dimitri.remoiville.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserFirestoreRepository {

    // Collection reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // Insert
    // Create
    public Task<Void> createUser(User user) {
        return getUsersCollection().document(user.getUserID()).set(user);
    }

    // Fetch
    // User with userID
    public Task<DocumentSnapshot> getUser(String userID) {
        return getUsersCollection().document(userID).get();
    }

    // All users sort by restaurant ID
    public Task<QuerySnapshot> getAllUsersSortByRestaurantID() {
        return getUsersCollection().orderBy("restaurantID", Query.Direction.DESCENDING).get();
    }

    // Get users with a placeID not null
    public Task<QuerySnapshot> getUsersPlaceIDNotNull() {
        return getUsersCollection().whereNotEqualTo("restaurantID", null).get();
    }


    // Get all users eating at specific restaurant
    public Task<QuerySnapshot> getUsersWithPlaceID(String placeID) {
        return getUsersCollection().whereEqualTo("restaurantID", placeID).get();
    }


    // Update
    public Task<Void> updateUserData(String userID, String firstName, String lastName, String mail, String urlPicture) {
        return getUsersCollection().document(userID).update("firstName", firstName,
                "lastName", lastName,
                "mail", mail,
                "urlprofilePicture", urlPicture);
    }

    public Task<Void> updateLunchID(String userID, String placeID, String restaurantName) {
        return getUsersCollection().document(userID).update("restaurantID", placeID,
                "restaurantName",restaurantName);
    }

    public Task<Void> updateLikesList(String userID, List<String> likesList) {
        return getUsersCollection().document(userID).update("likesList", likesList);
    }

    // Delete a specific user
    public Task<Void> deleteUser(String userID) {
        return getUsersCollection().document(userID).delete();
    }
}
