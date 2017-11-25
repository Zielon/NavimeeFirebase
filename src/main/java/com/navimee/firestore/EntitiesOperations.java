package com.navimee.firestore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.*;
import com.navimee.models.entities.Entity;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.linq.Distinct.distinctByKey;

public class EntitiesOperations {

    public static <T extends Entity> Future addToCollection(CollectionReference collectionReference, List<T> entities) {
        Map<String, T> entityMap = entities.stream().filter(distinctByKey(Entity::getId)).collect(Collectors.toMap(Entity::getId, Function.identity()));
        return addToCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public static <T extends Entity> Future addToCollection(CollectionReference collectionReference, T entity) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);
        return addToCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
    }

    public static <T extends Entity> Future addToCollection(CollectionReference collectionReference, T entity, AdditionEnum option) {
        Map<String, T> entityMap = new HashMap<>();
        entityMap.put(entity.getId(), entity);

        if(option == AdditionEnum.OVERWRITE)
            return addToCollection(collectionReference, entityMap, AdditionEnum.OVERWRITE);
        else
            return addToCollection(collectionReference, entityMap, AdditionEnum.MERGE);
    }

    public static <T extends Entity> Future addToCollection(CollectionReference collectionReference, Map<String, T> entities, AdditionEnum options) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            if(entities.size() == 0) return;
            try {
                ExecutorService executor = Executors.newWorkStealingPool();
                List<Callable<Object>> tasks = new ArrayList<>();
                for (Map.Entry<String, T> entry : entities.entrySet())
                    if(options == AdditionEnum.MERGE)
                        tasks.add(() -> collectionReference.document(entry.getKey()).set(entry.getValue(), SetOptions.merge()));
                    else
                        tasks.add(() -> collectionReference.document(entry.getKey()).set(entry.getValue()));

                // Wait for all tasks to finish.
                executor.invokeAll(tasks);

                String LOG = String.format("ENTITIES %d ADDED TO -> %s | %s", entities.size(), collectionReference.getPath().toUpperCase(), new Date());
                System.out.println(LOG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> List<T> getFromDocument(DocumentReference documentReference, Class<T> type) {
        List<T> output = new ArrayList<>();
        try {
            for (CollectionReference collectionReference : documentReference.getCollections().get())
                output.addAll(getFromCollection(collectionReference, type, false));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static <T> List<T> getFromCollection(CollectionReference collectionReference, Class<T> type) {
        return getFromCollection(collectionReference, type, true);
    }

    public static <T> List<T> getFromCollection(CollectionReference collectionReference, Class<T> type, boolean logging) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        List<T> output = new ArrayList<>();
        try {
            for (DocumentSnapshot document : collectionReference.get().get().getDocuments())
                output.add(mapper.convertValue(document.getData(), type));

            String LOG = String.format("ENTITIES %d RETRIEVED FROM -> %s | %s", output.size(), collectionReference.getPath().toUpperCase(), new Date());
            if(logging) System.out.println(LOG);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static void deleteCollection(CollectionReference collection) {
        int batchSize = 100;
        try {
            int deleted = 0;
            QuerySnapshot future = collection.limit(batchSize).get().get();
            List<DocumentSnapshot> documents = future.getDocuments();
            for (DocumentSnapshot document : documents) {
                document.getReference().delete().get();
                ++deleted;
            }
            if (deleted >= batchSize) {
                deleteCollection(collection);
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }

    public static void deleteDocument(DocumentReference document){
        try{
           for (CollectionReference collection : document.getCollections().get())
               deleteCollection(collection);
           document.delete().get();
        }catch (Exception e){
            System.err.println("Error deleting document : " + e.getMessage());
        }
    }
}
