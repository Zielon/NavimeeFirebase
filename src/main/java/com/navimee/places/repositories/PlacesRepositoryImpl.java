package com.navimee.places.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.contracts.models.dataTransferObjects.firestore.CityDto;
import com.navimee.contracts.models.dataTransferObjects.placeDetails.FoursquarePlaceDetailsDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestoreHelpers.EntitiesOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    @Autowired
    Firestore db;

    @Override
    public <T extends PlaceDto> List<T> getPlaces(String city, Class<T> type) {
        return EntitiesOperations.getFromDocument(db.collection(placesPath).document(city), type, placesChunks);
    }

    @Override
    public <T extends PlaceDto> List<T> getFoursquarePlaces(String city, Class<T> type) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesPath).document(city), type, placesChunks);
    }

    @Override
    public List<FoursquarePlaceDetailsDto> getFoursquarePlacesDetails(String city) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesDetailsPath).document(city), FoursquarePlaceDetailsDto.class, placesChunks);
    }

    @Override
    public Future setPlaces(List<? extends PlaceDto> places, String city) {
        DocumentReference targetDocument = db.collection(placesPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, places, p -> p.id, placesChunks);
    }

    @Override
    public Future setFoursquarePlaces(List<? extends PlaceDto> places, String city) {
        DocumentReference targetDocument = db.collection(foursquarePlacesPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, places, p -> p.id, placesChunks);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FoursquarePlaceDetailsDto> details, String city) {
        DocumentReference targetDocument = db.collection(foursquarePlacesDetailsPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, details, p -> p.id, placesChunks);
    }

    @Override
    public Future deleteCollection(String collection) {
        return Executors.newSingleThreadExecutor().submit(
                () -> EntitiesOperations.deleteCollection(db.collection(collection), getAvailableCities(), placesChunks));
    }

    @Override
    public Future setAvailableCities(List<CityDto> cities) {
        return Executors.newSingleThreadExecutor()
                .submit(() -> cities.forEach(city -> db.collection(availableCitiesPath).document(city.name).set(city)));
    }

    @Override
    public List<CityDto> getAvailableCities() {
        List<CityDto> cities = null;
        ApiFuture<QuerySnapshot> query = db.collection(availableCitiesPath).get();
        try {
            cities = query.get().getDocuments().stream().map(c -> c.toObject(CityDto.class)).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public Future setCoordinates(Map<String, List<CoordinateDto>> coordinatesMap) {
        return Executors.newSingleThreadExecutor()
                .submit(() -> coordinatesMap.keySet()
                        .forEach(city -> {
                            List<CoordinateDto> coordinates = coordinatesMap.get(city);
                            Map<String, CoordinateDto> map = IntStream.range(0, coordinates.size()).boxed().collect(toMap(i -> i.toString(), coordinates::get));
                            try {
                                EntitiesOperations.addToDocument(db.collection(coordinatesPath).document(city), map).get();
                            } catch (Exception e) {
                            }
                        }));
    }

    @Override
    public List<CoordinateDto> getCoordinates(String city) {
        return EntitiesOperations.getFromDocument(db.collection(coordinatesPath).document(city), CoordinateDto.class);
    }
}
