package com.navimee.contracts.repositories;

import com.navimee.models.entities.User;

import java.util.List;

public interface UsersRepository {
    List<User> getUsersForNotification();
}
