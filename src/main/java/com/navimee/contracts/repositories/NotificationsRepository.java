package com.navimee.contracts.repositories;

import com.navimee.models.entities.Notification;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationsRepository {

    CompletableFuture<List<Notification>> getAvailableNotifications();

    CompletableFuture<List<Notification>> getBigEventsNotifications();
}
