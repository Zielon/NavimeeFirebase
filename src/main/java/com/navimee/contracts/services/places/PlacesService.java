package com.navimee.contracts.services.places;

import com.navimee.models.bussinesObjects.general.CoordinateBo;
import com.navimee.models.bussinesObjects.places.FsPlaceBo;
import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.externalDto.geocoding.GooglePlaceDto;

import java.util.List;

public interface PlacesService {
    List<PlaceBo> downloadFacebookPlaces(String city);

    List<FsPlaceBo> downloadFoursquarePlaces(String city);

    List<FsPlaceDetailsBo> downloadFoursquarePlacesDetails(String city);

    GooglePlaceDto downloadReverseGeocoding(CoordinateBo coordinate);
}
