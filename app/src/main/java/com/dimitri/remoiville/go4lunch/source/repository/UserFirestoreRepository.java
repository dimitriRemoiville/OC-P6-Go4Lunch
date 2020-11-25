package com.dimitri.remoiville.go4lunch.source.repository;

import com.dimitri.remoiville.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFirestoreRepository {

    // Collection reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // Create
    public Task<Void> createUser(String userID, String lastName, String firstName, String eMail, String urlProfilePicture) {
        User user = new User(userID, lastName, firstName, eMail, urlProfilePicture);
        return getUsersCollection().document(userID).set(user);
    }

    // Get
    public Task<DocumentSnapshot> getUser(String userID) {
        return getUsersCollection().document(userID).get();
    }

    // Update
    public Task<Void> updateLastName(String userID, String lastName) {
        return getUsersCollection().document(userID).update("lastName", lastName);
    }

    public Task<Void> updateFirstName(String userID, String firstName) {
        return getUsersCollection().document(userID).update("firstName", firstName);
    }

    public Task<Void> updateUsername(String userID, String eMail) {
        return getUsersCollection().document(userID).update("email", eMail);
    }

    public Task<Void> updateURLPicture(String userID, String urlPicture) {
        return getUsersCollection().document(userID).update("urlPicture", urlPicture);
    }

    // Delete
    public Task<Void> deleteUser(String userID) {
        return getUsersCollection().document(userID).delete();
    }
}
