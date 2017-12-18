package com.navimee.contracts.repositories;

import com.navimee.models.entities.User;

public interface UsersRepository {
    User getUser(String id);
}
