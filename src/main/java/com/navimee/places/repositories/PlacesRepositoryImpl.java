package com.navimee.places.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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

import static com.navimee.firestoreHelpers.Distinct.distinctByKey;

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

    private <T extends Place> List<T> getPlacesFromRepository(String city, Class<T> type, String path) {
        List<T> places = new ArrayList<>();
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(path).document(city).get();
        ObjectMapper mapper = new ObjectMapper();
        try {
            DocumentSnapshot snapshot = documentSnapshot.get();
            QuerySnapshot chunks = snapshot.getReference().collection(placesChunks).get().get();
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
    public <T extends Place> List<T> getPlaces(String city, Class<T> type) {
        return getPlacesFromRepository(city, type, placesPath);
    }

    @Override
    public <T extends Place> List<T> getFoursquarePlaces(String city, Class<T> type) {
        return getPlacesFromRepository(city, type, foursquarePlacesPath);
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
    public Future setPlaces(List<? extends Place> places, String city) {
        return addPlacesToRepository(places, city, placesPath, "PLACES ADDED ");
    }

    @Override
    public Future setFoursquarePlaces(List<? extends Place> places, String city) {
        return addPlacesToRepository(places, city, foursquarePlacesPath, "FOURSQUARE PLACES ADDED ");
    }

    private Future addPlacesToRepository(List<? extends Place> places, String city, String path, String log) {
        int chunkSize = 2000;
        return Executors.newSingleThreadExecutor().submit(
                () -> {
                    try {
                        Map<String, Place> placesMap = places.stream()
                                .filter(distinctByKey(p -> p.getId()))
                                .collect(Collectors.toMap(Place::getId, Function.identity()));

                        if (placesMap.size() > chunkSize)
                            for (Map<String, Place> map : TransactionSplit.mapSplit(placesMap, chunkSize))
                                db.collection(path).document(city).collection(placesChunks).document().set(map).get();
                        else
                            db.collection(path).document(city).set(placesMap).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    System.out.println(log + city + " at " + new Date());
                });
    }

    @Override
    public Future deleteCollection(String collection) {

        return Executors.newSingleThreadExecutor().submit(
                () -> deleteCollection(db.collection(collection), 2)
        );
    }

    private void deleteCollection(CollectionReference collection, int batchSize) {
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<DocumentSnapshot> documents = future.get().getDocuments();
            for (DocumentSnapshot document : documents) {
                DocumentReference reference = document.getReference();
                reference.getCollections().get().forEach(c -> {
                    deleteCollection(c, 2);
                });
                reference.delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                // retrieve and delete another batch
                deleteCollection(collection, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }
}
