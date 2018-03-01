package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Repository
public class FirestoreRepositoryImpl implements FirestoreRepository {

    @Autowired
    DbDelete dbDelete;

    @Autowired
    Firestore firestore;

    @Autowired
    ExecutorService executorService;

    @Override
    public CompletableFuture<Void> deleteDocument(String document) {
        return CompletableFuture
                .runAsync(() -> dbDelete.document(firestore.document(document)), executorService)
                .thenRunAsync(() -> Logger.LOG(new Log(LogTypes.DELETION, "The [%s] was deleted", document)));
    }

    @Override
    public CompletableFuture<Void> deleteCollection(String collection) {
        return CompletableFuture
                .runAsync(() -> dbDelete.collection(firestore.collection(collection), 1), executorService)
                .thenRunAsync(() -> Logger.LOG(new Log(LogTypes.DELETION, "The [%s] was deleted", collection)));
    }
}