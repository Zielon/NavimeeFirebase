package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestore.operations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.navimee.firestore.Paths.BY_CITY;

@Repository
public class FirestoreRepositoryImpl implements FirestoreRepository {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    Delete delete;

    @Autowired
    Firestore db;

    @Override
    public void deleteDocument(String document) {

    }

    @Override
    public void deleteCollection(String collection) {
        placesRepository.getAvailableCities().forEach(city ->
                delete.collection(db.collection(collection).document(BY_CITY).collection(city.getName()), 1));
    }
}
