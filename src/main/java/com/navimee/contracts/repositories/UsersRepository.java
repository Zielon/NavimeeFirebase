package com.navimee.contracts.repositories;

import com.navimee.models.entities.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UsersRepository {

    CompletableFuture<User> getUser(String id);

    CompletableFuture<List<User>> getAllUsers();

    CompletableFuture<Void> updateUsersField(String fieldName, Object value) throws NoSuchFieldException;

    CompletableFuture<Void> deleteUsersField(String fieldName) throws NoSuchFieldException;

    CompletableFuture<Void> deleteUsersCollection(String collection);

    CompletableFuture<List<User>> getUsersWithBigEventsOn();
}
