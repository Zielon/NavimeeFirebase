package com.navimee.contracts.repositories;

public interface FirestoreRepository {
    void deleteDocument(String document);
    void deleteCollection(String collection);
}
