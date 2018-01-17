package com.navimee.models.entities.contracts;

import java.util.Map;

public interface FcmSendable {
    String getToken();

    boolean isSent();

    void setSent(boolean sent);

    String getId();

    Map<String, Object> toDictionary();
}
