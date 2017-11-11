package com.navimee.places.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.firestore.Coordinates;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestoreHelpers.TransactionSplit;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public List<Coordinate> getCoordinates(String city) {
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(coordinatesPath).document(city).get();
        DocumentSnapshot document = null;
        try {
            document = documentSnapshot.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return document.toObject(Coordinates.class).points;
    }

    @Override
    public List<City> getAvailableCities() {
        List<City> cities = null;
        ApiFuture<QuerySnapshot> query = db.collection(availableCitiesPath).get();
        try {
            cities = query.get().getDocuments().stream().map(c -> c.toObject(City.class)).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public <T extends Place> List<T> getPlaces(String city, Class<T> type) {
        List<T> places = new ArrayList<>();
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(placesPath).document(city).get();
        ObjectMapper mapper = new ObjectMapper();
        try {
            DocumentSnapshot snapshot = documentSnapshot.get();
            QuerySnapshot chunks = snapshot.getReference().collection(eventsChunks).get().get();
            if (!chunks.isEmpty())
                chunks.getDocuments().forEach(d -> d.getData().forEach((k, v) -> places.add(mapper.convertValue(v, type))));
            else
                snapshot.getData().forEach((k, v) -> places.add(mapper.convertValue(v, type)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    public Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {

        return Executors.newSingleThreadExecutor().submit(
                () -> coordinatesMap.keySet().forEach(city -> {
                            List<Coordinate> coordinates = coordinatesMap.get(city);
                            Map<String, List<Coordinate>> elements = new HashMap<>();
                            elements.put("points", coordinates);
                            try {
                                db.collection(coordinatesPath).document(city).set(elements).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                ));
    }

    @Override
    public Future setAvailableCities(List<City> cities) {

        return Executors.newSingleThreadExecutor().submit(
                () -> cities.forEach(c -> {
                    try {
                        db.collection(availableCitiesPath).document(c.name).set(c).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public Future setPlaces(List<Place> places, String city) {

        return Executors.newSingleThreadExecutor().submit(
                () -> {
                    try {
                        Map<String, Place> p = places.stream().collect(Collectors.toMap(Place::getId, Function.identity()));
                        if (p.size() > 6000)
                            for (Map<String, Place> map : TransactionSplit.mapSplit(p, 6000))
                                db.collection(placesPath).document(city)
                                        .collection(eventsChunks)
                                        .document().set(map, SetOptions.merge()).get();
                        else
                            db.collection(placesPath).document(city).set(p, SetOptions.merge()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PLACES ADDED " + city + " at " + new Date());
                });
    }

    @Override
    public Future deleteCollection(String collection) {

        return Executors.newSingleThreadExecutor().submit(
                () -> {
                    try {
                        List<DocumentSnapshot> documents = db.collection(collection).get().get().getDocuments();
                        for (DocumentSnapshot document : documents)
                            document.getReference().delete().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
