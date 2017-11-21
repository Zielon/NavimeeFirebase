package com.navimee.places.googleGeocoding;

import com.navimee.contracts.models.places.GooglePlace;
import com.navimee.contracts.models.places.pojo.AddressComponents;
import com.navimee.places.googleGeocoding.enums.GeoNameType;
import com.navimee.places.googleGeocoding.enums.GeoType;

public class GoogleGeoTypeGetter {

    public static String getType(GooglePlace place, GeoType type) {
        return getType(place, type, GeoNameType.long_name);
    }

    public static String getType(GooglePlace place, GeoType type, GeoNameType nameType) {
        AddressComponents addressComponents =
                place.addressComponents.stream().peek(address -> address.types.contains(type.toString())).findFirst().get();

        return nameType == GeoNameType.long_name ? addressComponents.longName : addressComponents.shortName;
    }
}
