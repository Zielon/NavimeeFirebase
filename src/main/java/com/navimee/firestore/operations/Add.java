package com.navimee.firestore.operations;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.navimee.firestore.operations.enums.AdditionEnum;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleFuture;
import static com.navimee.linq.Distinct.distinctByKey;

@Component
public class Add {

    @Autowired
    ExecutorService executorService;

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, List<T> entities) {
        Map<String, T> entityMap = entities.stream()
                .filter(distinctByKey(Entity::getId))
                .peek(entity -> entity.setInternalId(UUID.randomUUID()))
                .collect(Collectors.toMap(Entity::getInternalId, Function.identity()));
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity) {
        Map<String, T> entityMap = new HashMap<>();
        entity.setInternalId(UUID.randomUUID());
        entityMap.put(entity.getInternalId(), entity);
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity, AdditionEnum option) {
        Map<String, T> entityMap = new HashMap<>();
        entity.setInternalId(UUID.randomUUID());
        entityMap.put(entity.getInternalId(), entity);
        return toCollection(collectionReference, entityMap, option);
    }

    private <T extends Entity> Future toCollection(CollectionReference collectionReference, Map<String, T> entities, AdditionEnum options) {
        return executorService.submit(() -> {
            if (entities.size() == 0) return;
            try {
                List<Future<WriteResult>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet()) {
                    if (options == AdditionEnum.MERGE)
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue(), SetOptions.merge()));
                    else
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue()));
                }
                // Wait for all tasks to finish.
                waitForSingleFuture(executorService, tasks);

                Logger.LOG(new Log(LogEnum.ADDITION, collectionReference.getPath(), entities.size()));

            } catch (Exception e) {
                Logger.LOG(new Log(LogEnum.EXCEPTION, e));
            }
        });
    }
}
