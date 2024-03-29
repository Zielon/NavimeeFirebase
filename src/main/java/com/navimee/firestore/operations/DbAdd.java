package com.navimee.firestore.operations;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.navimee.asyncCollectors.Completable;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.contracts.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.linq.Distinct.distinctByKey;

@Component
public class DbAdd {

    @Autowired
    ExecutorService executorService;

    public <T extends Entity> CompletableFuture<Void> toCollection(CollectionReference collectionReference, List<T> entities) {
        Map<String, T> entityMap = entities.stream()
                .filter(distinctByKey(Entity::getId))
                .collect(Collectors.toMap(Entity::getId, Function.identity()));

        return toCollection(collectionReference, entityMap, null);
    }

    public <T extends Entity> CompletableFuture<Void> toCollection(CollectionReference collectionReference, T entity) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);
        return toCollection(collectionReference, entityMap, null);
    }

    public <T extends Entity> CompletableFuture<Void> toCollection(CollectionReference collectionReference, Map<String, T> entities, SetOptions options) {
        return CompletableFuture.runAsync(() -> {
            if (entities.size() == 0) return;

            try {
                List<Future<WriteResult>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet()) {
                    if (options != null)
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue(), options));
                    else
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue()));
                }
                // Wait for all tasks to finish.
                Completable.wait(executorService, tasks);

            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
        }, executorService);
    }
}
