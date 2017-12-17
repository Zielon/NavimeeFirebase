package com.navimee.contracts.repositories;

import com.navimee.models.entities.Notification;

import java.util.List;

public interface NotificationsRepository {
    List<Notification> getAvailable();
}
