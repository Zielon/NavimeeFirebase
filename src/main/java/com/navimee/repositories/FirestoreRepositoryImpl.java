package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.firestore.operations.DbDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.navimee.firestore.FirebasePaths.BY_CITY;

@Repository
public class FirestoreRepositoryImpl implements FirestoreRepository {

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    DbDelete delete;

    @Autowired
    Firestore db;

    @Override
    public void deleteDocument(String document) {
        delete.collection(db.collection(document), 1);
    }

    @Override
    public void deleteCollection(String collection) {
        coordinatesRepository.getAvailableCities()
                .thenAcceptAsync(cities -> cities.forEach(city ->
                        delete.collection(db.collection(collection).document(BY_CITY).collection(city.getName()), 1)));
    }
}
