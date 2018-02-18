package com.navimee.firestore.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.cloud.firestore.*;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.contracts.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
public class DbGet {

    @Autowired
    ExecutorService executorService;

    private ObjectMapper mapper;

    public DbGet() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }

    public <T extends Entity> CompletableFuture<List<T>> fromCollection(CollectionReference collectionReference, Class<T> type) {
        CompletableFuture<List<T>> output = null;
        try {
            output = getData(collectionReference.get().get().getDocuments(), type);
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        return output;
    }

    public <T extends Entity> CompletableFuture<List<T>> fromQuery(Query query, Class<T> type) {
        CompletableFuture<List<T>> output = null;
        try {
            output = getData(query.get().get().getDocuments(), type);
        } catch (Exception e) {
            Logger.LOG(new Log(LogTypes.EXCEPTION, e));
        }

        return output;
    }

    public <T extends Entity> CompletableFuture<T> fromDocument(DocumentReference documentReference, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            T entity = null;
            try {
                entity = mapper.convertValue(documentReference.get().get().getData(), type);
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
            return entity;
        }, executorService);
    }

    public <T extends Entity> CompletableFuture<List<T>> fromDocumentCollection(DocumentReference documentReference, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            List<T> output = new ArrayList<>();
            documentReference.getCollections().forEach(collectionReference -> {
                output.addAll(fromCollection(collectionReference, type).join());
            });
            return output;
        }, executorService);
    }

    private <T extends Entity> CompletableFuture<List<T>> getData(List<QueryDocumentSnapshot> documentSnapshots, Class<T> type) {
        return CompletableFuture.supplyAsync(() -> {
            List<T> output = new ArrayList<>();
            try {
                for (DocumentSnapshot document : documentSnapshots) {
                    T entity;
                    try {
                        entity = mapper.convertValue(document.getData(), type);
                        output.add(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Logger.LOG(new Log(LogTypes.EXCEPTION, e));
            }
            return output;
        }, executorService);
    }
}
