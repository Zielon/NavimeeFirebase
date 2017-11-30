package com.navimee.contracts.services;

import java.util.concurrent.Future;

public interface Notifications {
    Future send(String userToken);
}
