package com.navimee.firestoreHelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.models.entities.general.City;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.linq.Distinct.distinctByKey;

public class EntitiesOperations {

    public static <T> Future addToDocument(CollectionReference collectionReference, List<T> entities, Function<T, String> func) {
        Map<String, T> entityMap = entities.stream().filter(distinctByKey(func)).collect(Collectors.toMap(func, Function.identity()));
        return addToDocument(collectionReference, entityMap);
    }

    public static <V> Future addToDocument(CollectionReference collectionReference, Map<String, V> entities) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                ExecutorService executor = Executors.newWorkStealingPool();
                List<Callable<Object>> tasks = new ArrayList<>();
                for (Map.Entry<String, V> entry : entities.entrySet())
                    tasks.add(() -> collectionReference.document(entry.getKey()).set(entry.getValue()).get());

                // Wait for all tasks to finish.
                executor.invokeAll(tasks);
                System.out.println("ENTITIES ADDED TO -> " + collectionReference.getPath().toUpperCase() + " | " + new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> List<T> getFromDocument(DocumentReference targetDocument, Class<T> type) {
        return getFromDocument(targetDocument, type, "");
    }

    public static <T> List<T> getFromDocument(DocumentReference targetDocument, Class<T> type, String chunkName) {
        ObjectMapper mapper = new ObjectMapper();
        List<T> output = new ArrayList<>();
        try {
            DocumentSnapshot snapshot = targetDocument.get().get();
            QuerySnapshot chunks = snapshot.getReference().collection(chunkName).get().get();
            if (!chunks.isEmpty())
                chunks.getDocuments().forEach(d -> d.getData().forEach((k, v) -> output.add(mapper.convertValue(v, type))));
            else
                snapshot.getData().forEach((k, v) -> output.add(mapper.convertValue(v, type)));

            System.out.println("ENTITIES RETRIEVED FROM -> " + targetDocument.getPath().toUpperCase() + " | " + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void deleteCollection(CollectionReference collection) {
        int batchSize = 1000;
        try {
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            future.get();
            List<DocumentSnapshot> documents = future.get().getDocuments();
            for (DocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                deleteCollection(collection);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }
}
