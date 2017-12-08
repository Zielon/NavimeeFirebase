package com.navimee.repositories.inmemory;

import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository {

    private final static Map<String, List<FsPlace>> FoursquarePlaces = new HashMap<>();
    private final static Map<String, List<FbPlace>> FacebookPlaces = new HashMap<>();

    public static List<FbPlace> GET_FACEBOOK(String city) {
        synchronized (FacebookPlaces) {
            if (FacebookPlaces.containsKey(city)) {
                List<FbPlace> places = FacebookPlaces.get(city);
                Logger.LOG(new Log(LogEnum.RETRIEVAL_IN_MEMORY,
                        String.format("GET [%s] FACEBOOK PLACES [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                return places;
            } else return null;
        }
    }

    public static void SET_FACEBOOK(String city, List<FbPlace> places) {
        synchronized (FacebookPlaces) {
            if (!FacebookPlaces.containsKey(city)) {
                Logger.LOG(new Log(LogEnum.ADDITION_IN_MEMORY,
                        String.format("SET [%s] FACEBOOK PLACES [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                FacebookPlaces.put(city, places);
            }
        }
    }

    public static List<FsPlace> GET_FOURSQUARE(String city) {
        synchronized (FoursquarePlaces) {
            if (FoursquarePlaces.containsKey(city)) {
                List<FsPlace> places = FoursquarePlaces.get(city);
                Logger.LOG(new Log(LogEnum.RETRIEVAL_IN_MEMORY,
                        String.format("GET [%s] FOURSQUARE PLACES [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                return places;
            } else return null;
        }
    }

    public static void SET_FOURSQUARE(String city, List<FsPlace> places) {
        synchronized (FoursquarePlaces) {
            if (!FoursquarePlaces.containsKey(city)) {
                Logger.LOG(new Log(LogEnum.ADDITION_IN_MEMORY,
                        String.format("SET [%s] FOURSQUARE PLACES [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                FoursquarePlaces.put(city, places);
            }
        }
    }
}
