package com.navimee.places.services;

import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.services.places.PlacesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacesServiceImpl implements PlacesService {
    @Override
    public List<Place> getFacebookPlaces(List<Coordinate> coordinates) {
        return null;
    }

    @Override
    public List<Place> getFoursquarePlaces(List<Coordinate> coordinates) {
        return null;
    }
}
