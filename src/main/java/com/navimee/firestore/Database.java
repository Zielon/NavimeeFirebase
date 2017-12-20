package com.navimee.firestore;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.navimee.enums.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Database {

    @Autowired
    Firestore db;

    public CollectionReference getCollectionByCity(CollectionType collection, String city) {
        return db.collection(collection.toString()).document(Paths.BY_CITY).collection(city);
    }

    public DocumentReference getDocumentByCity(CollectionType collection) {
        return db.collection(collection.toString()).document(Paths.BY_CITY);
    }

    public CollectionReference getCollection(CollectionType collection) {
        return db.collection(collection.toString());
    }
}