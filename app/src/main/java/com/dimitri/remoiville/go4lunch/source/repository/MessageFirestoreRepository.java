package com.dimitri.remoiville.go4lunch.source.repository;

import com.dimitri.remoiville.go4lunch.model.Message;
import com.dimitri.remoiville.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MessageFirestoreRepository {

    // Collection reference
    public CollectionReference getMessagesCollection() {
        return FirebaseFirestore.getInstance().collection("messages");
    }

    // --- GET ---
    // All messages sorted by date
    public Query getLastMessages() {
        return getMessagesCollection()
                .orderBy("dateCreated")
                .limit(50);
    }

    // --- CREATE ---

    public Task<DocumentReference> createMessageForChat(String textMessage, User userSender){
        Message message = new Message(textMessage, userSender);
        return getMessagesCollection().add(message);
    }
}
