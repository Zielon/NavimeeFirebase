package com.navimee.firestore.operations;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.navimee.firestore.operations.enums.AdditionEnum;
import com.navimee.logger.Log;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleFuture;
import static com.navimee.linq.Distinct.distinctByKey;

public class Add extends Base {

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
        return executorService.submit(() -> {
            if (entities.size() == 0) return;
            try {
                List<Future<WriteResult>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet())
                    if (options == AdditionEnum.MERGE)
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue(), SetOptions.merge()));
                    else
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue()));

                // Wait for all tasks to finish.
                waitForSingleFuture(executorService, tasks);

                Logger.LOG(new Log(LogEnum.ADDITION, collectionReference.getPath(), entities.size()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
