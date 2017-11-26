package com.navimee.firestore.operations;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.navimee.models.entities.Entity;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingle;
import static com.navimee.linq.Distinct.distinctByKey;

public class Add {

    public static <T extends Entity> Future toCollection(CollectionReference collectionReference, List<T> entities) {
        Map<String, T> entityMap = entities.stream().filter(distinctByKey(Entity::getId)).collect(Collectors.toMap(Entity::getId, Function.identity()));
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public static <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public static <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity, AdditionEnum option) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);

        if (option == AdditionEnum.OVERWRITE)
            return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
        else
            return toCollection(collectionReference, entityMap, AdditionEnum.MERGE);
    }

    public static <T extends Entity> Future toCollection(CollectionReference collectionReference, Map<String, T> entities, AdditionEnum options) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            if (entities.size() == 0) return;
            try {
                List<Future<WriteResult>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet())
                    if (options == AdditionEnum.MERGE)
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue(), SetOptions.merge()));
                    else
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue()));

                // Wait for all tasks to finish.
                waitForSingle(tasks);

                String LOG = String.format("ENTITIES %d ADDED TO -> %s | %s", entities.size(), collectionReference.getPath().toUpperCase(), new Date());
                System.out.println(LOG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
