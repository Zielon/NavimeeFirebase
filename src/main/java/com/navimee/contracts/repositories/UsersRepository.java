package com.navimee.contracts.repositories;

import com.navimee.models.entities.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UsersRepository {
    CompletableFuture<User> getUser(String id);

    CompletableFuture<List<User>> getUsersWithBigEventsOn();
}
