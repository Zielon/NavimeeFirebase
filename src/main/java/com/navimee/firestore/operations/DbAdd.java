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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForSingleFuture;
import static com.navimee.linq.Distinct.distinctByKey;

@Component
public class DbAdd {

    @Autowired
    ExecutorService executorService;

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, List<T> entities) {
        Map<String, T> entityMap = entities.stream()
                .filter(distinctByKey(Entity::getId))
                .collect(Collectors.toMap(Entity::getId, Function.identity()));
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);
        return toCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public <T extends Entity> Future toCollection(CollectionReference collectionReference, T entity, AdditionEnum option) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);
        return toCollection(collectionReference, entityMap, option);
    }

    private <T extends Entity> Future toCollection(CollectionReference collectionReference, Map<String, T> entities, AdditionEnum options) {
        return executorService.submit(() -> {
            if (entities.size() == 0) return;
            String type = null;
            try {
                List<Future<WriteResult>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet()) {
                    type = entry.getValue().getClass().getSimpleName();
                    if (options == AdditionEnum.MERGE)
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue(), SetOptions.merge()));
                    else
                        tasks.add(collectionReference.document(entry.getKey()).set(entry.getValue()));
                }
                // Wait for all tasks to finish.
                waitForSingleFuture(executorService, tasks);

                Logger.LOG(new Log(LogEnum.ADDITION,
                        String.format("%s | [Type: %s]", collectionReference.getPath(), type),
                        entities.size()));

            } catch (Exception e) {
                Logger.LOG(new Log(LogEnum.EXCEPTION, e));
            }
        });
    }
}
