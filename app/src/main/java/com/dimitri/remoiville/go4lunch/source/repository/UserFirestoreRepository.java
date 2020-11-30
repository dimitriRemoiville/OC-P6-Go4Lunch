package com.dimitri.remoiville.go4lunch.source.repository;

import com.dimitri.remoiville.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserFirestoreRepository {

    // Collection reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // Create
    public Task<Void> createUser(String userID, String name, String eMail) {
        User user = new User(userID, name, eMail);
        return getUsersCollection().document(userID).set(user);
    }

    // Get
    // User with userID
    public Task<DocumentSnapshot> getUser(String userID) {
        return getUsersCollection().document(userID).get();
    }
    // All users
    public Task<QuerySnapshot> getAllUsers() {
        return getUsersCollection().get();
    }

    // Update
    public Task<Void> updateName(String userID, String name) {
        return getUsersCollection().document(userID).update("name", name);
    }

    public Task<Void> updateUsername(String userID, String eMail) {
        return getUsersCollection().document(userID).update("email", eMail);
    }

    public Task<Void> updateURLPicture(String userID, String urlPicture) {
        return getUsersCollection().document(userID).update("url_picture", urlPicture);
    }

    // Delete
    public Task<Void> deleteUser(String userID) {
        return getUsersCollection().document(userID).delete();
    }
}
