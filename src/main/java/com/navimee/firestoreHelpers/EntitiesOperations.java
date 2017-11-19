package com.navimee.firestoreHelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.contracts.models.firestore.City;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.firestoreHelpers.Distinct.distinctByKey;

public class EntitiesOperations {

    public static <T> Future addToDocument(DocumentReference targetDocument, List<T> entities, Function<T, String> func) {
        Map<String, T> entityMap = entities.stream().filter(distinctByKey(func)).collect(Collectors.toMap(func, Function.identity()));
        return addToDocument(targetDocument, entityMap, "", 2000);
    }

    public static <T> Future addToDocument(DocumentReference targetDocument, List<T> entities, Function<T, String> func, String chunkName) {
        Map<String, T> entityMap = entities.stream().filter(distinctByKey(func)).collect(Collectors.toMap(func, Function.identity()));
        return addToDocument(targetDocument, entityMap, chunkName, 2000);
    }

    public static <K, V> Future addToDocument(DocumentReference targetDocument, Map<K, V> entities, String chunkName) {
        return addToDocument(targetDocument, entities, chunkName, 2000);
    }

    public static <K, V> Future addToDocument(DocumentReference targetDocument, Map<K, V> entities) {
        return addToDocument(targetDocument, entities, "", 2000);
    }

    public static <K, V> Future addToDocument(DocumentReference targetDocument, Map<K, V> entities, String chunkName, int chunkSize) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                if (entities.size() > chunkSize && !chunkName.isEmpty())
                    for (Map<K, V> map : TransactionSplit.mapSplit(entities, chunkSize))
                        targetDocument.collection(chunkName).document().set(map).get();
                else
                    targetDocument.set(entities).get();

                System.out.println("ENTITIES ADDED TO -> " + targetDocument.getPath().toUpperCase() + " | " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
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

    public static void deleteCollection(CollectionReference collection, List<City> availableCities, String chunkName) {
        availableCities.forEach(city -> {
            ApiFuture<DocumentSnapshot> documentSnapshot = collection.document(city.name).get();
            try {
                DocumentSnapshot snapshot = documentSnapshot.get();
                QuerySnapshot chunks = snapshot.getReference().collection(chunkName).get().get();
                if (!chunks.isEmpty())
                    for(DocumentSnapshot d : chunks.getDocuments()) d.getReference().delete().get();
                else
                    snapshot.getReference().delete().get();

                System.out.println("COLLECTION IS DELETED FROM -> " + collection.getPath().toUpperCase() + " | " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
