package com.navimee.places.repositories;

import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.facebook.FbPlace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository {

    // Facebook places stored in memory.
    private final static Map<String, List<FbPlace>> FacebookPlaces = new HashMap<>();

    public static List<FbPlace> GET(String city) {
        synchronized (FacebookPlaces) {
            if (FacebookPlaces.containsKey(city)) {
                Logger.LOG(new Log(LogEnum.RETRIEVAL_IN_MEMORY,
                        String.format("GET [%s] FACEBOOK PLACES [IN-MEMORY]", city.toUpperCase()),
                        FacebookPlaces.get(city).size()));

                return FacebookPlaces.get(city);
            } else return null;
        }
    }

    public static void SET(String city, List<FbPlace> places) {
        synchronized (FacebookPlaces) {
            if (!FacebookPlaces.containsKey(city)) {
                Logger.LOG(new Log(LogEnum.ADDITION_IN_MEMORY,
                        String.format("SET [%s] FACEBOOK PLACES [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                FacebookPlaces.put(city, places);
            }
        }
    }
}
