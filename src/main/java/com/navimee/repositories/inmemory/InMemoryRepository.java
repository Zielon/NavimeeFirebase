package com.navimee.repositories.inmemory;

import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class InMemoryRepository {

    private final static Map<String, List<Entity>> Entities = new HashMap<>();

    public static <T extends Entity> List<T> GET(Class<T> type) {
        return GET("", type);
    }

    public static <T extends Entity> void SET(List<T> entities, Class<T> type) {
        SET("", entities, type);
    }

    public static <T extends Entity> List<T> GET(String key, Class<T> type) {
        synchronized (Entities) {
            key += type.getTypeName();
            if (Entities.containsKey(key)) {
                List<T> places = Entities.get(key).stream().map(entity -> (T) entity).collect(toList());
                Logger.LOG(new Log(LogTypes.RETRIEVAL_IN_MEMORY,
                        String.format("GET -> [type: %s] IN-MEMORY", type.getSimpleName()),
                        places.size()));

                return places;
            } else return null;
        }
    }

    public static <T extends Entity> void SET(String key, List<T> entities, Class<T> type) {
        synchronized (Entities) {
            key += type.getTypeName();
            if (!Entities.containsKey(key)) {
                Logger.LOG(new Log(LogTypes.ADDITION_IN_MEMORY,
                        String.format("SET -> [type: %s] IN-MEMORY", type.getSimpleName()),
                        entities.size()));

                Entities.put(key, entities.stream().map(entity -> (Entity) entity).collect(toList()));
            }
        }
    }
}
