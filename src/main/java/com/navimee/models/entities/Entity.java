package com.navimee.models.entities;

import java.util.UUID;

public interface Entity {
    String getId();

    String getInternalId();

    void setInternalId(UUID uuid);
}
