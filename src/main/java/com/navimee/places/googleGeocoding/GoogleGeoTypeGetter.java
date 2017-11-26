package com.navimee.places.googleGeocoding;

import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.dto.geocoding.subelement.AddressComponentsDto;
import com.navimee.places.googleGeocoding.enums.GeoNameType;
import com.navimee.places.googleGeocoding.enums.GeoType;

public class GoogleGeoTypeGetter {

    public static String getType(GooglePlaceDto place, GeoType type) {
        return getType(place, type, GeoNameType.long_name);
    }

    public static String getType(GooglePlaceDto place, GeoType type, GeoNameType nameType) {
        AddressComponentsDto addressComponents =
                place.addressComponents.stream().peek(address -> address.types.contains(type.toString())).findFirst().get();

        return nameType == GeoNameType.long_name ? addressComponents.longName : addressComponents.shortName;
    }
}
