package com.navimee.contracts.repositories;

import java.util.concurrent.CompletableFuture;

public interface ChatRepository {

    CompletableFuture<Void> setDefaultRooms();
}
