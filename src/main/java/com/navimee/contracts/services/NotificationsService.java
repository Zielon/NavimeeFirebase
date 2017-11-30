package com.navimee.contracts.services;

import com.navimee.models.entities.User;

import java.util.List;
import java.util.concurrent.Future;

public interface NotificationsService {
    Future send(List<User> users);
}
