package com.navimee.repositories.inmemory;

import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import org.hibernate.validator.internal.xml.ClassType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InMemoryRepository {

    private final static Map<String, List<Entity>> Entities = new HashMap<>();

    public static <T extends Entity> List<T> GET(String city) {
        synchronized (Entities) {
            if (Entities.containsKey(city)) {
                List<T> places = Entities.get(city).stream().map(entity -> (T)entity).collect(toList());
                Logger.LOG(new Log(LogEnum.RETRIEVAL_IN_MEMORY,
                        String.format("GET [%s] [IN-MEMORY]", city.toUpperCase()),
                        places.size()));

                return places;
            } else return null;
        }
    }

    public static <T extends Entity> void SET(String city, List<T> entities) {
        synchronized (Entities) {
            if (!Entities.containsKey(city)) {
                Logger.LOG(new Log(LogEnum.ADDITION_IN_MEMORY,
                        String.format("SET [%s] [IN-MEMORY]", city.toUpperCase()),
                        entities.size()));

                Entities.put(city, entities.stream().map(entity -> (Entity)entity).collect(toList()));
            }
        }
    }
}
