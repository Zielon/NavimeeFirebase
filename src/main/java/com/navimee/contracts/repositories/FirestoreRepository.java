package com.navimee.contracts.repositories;

import java.util.concurrent.CompletableFuture;

public interface FirestoreRepository {

    CompletableFuture<Void> deleteDocument(String document);

    CompletableFuture<Void> deleteCollection(String collection);
}
