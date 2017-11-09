package com.navimee.places.repositories;

import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.contracts.models.places.Coordinate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public List<Coordinate> getCoordinates(String city) {
        ApiFuture<QuerySnapshot> query = db.collection(String.format("%s/%s", coordinatesPath, city)).get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
        return null;
    }

    @Override
    public List<String> getAvailableCities() {
        return null;
    }

    @Override
    public void setCoordinates() {

    }

    @Override
    public void setAvailableCities() {

    }
}
