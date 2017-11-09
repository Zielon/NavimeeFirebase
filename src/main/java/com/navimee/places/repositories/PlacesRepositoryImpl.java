package com.navimee.places.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.places.Coordinates;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public List<Coordinate> getCoordinates(String city) {
        ApiFuture<QuerySnapshot> query = db.collection(coordinatesPath).get();
        return null;
    }

    @Override
    public List<String> getAvailableCities() {
        return null;
    }

    @Override
    public void setCoordinates() {
        CollectionReference collection = db.collection(coordinatesPath);
        Coordinates.Get().keySet().forEach(k -> collection.document(k).set(Coordinates.Get().get(k)));
    }

    @Override
    public void setAvailableCities() {

    }
}
