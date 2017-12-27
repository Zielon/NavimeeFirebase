package com.navimee.models.entities.contracts;

public interface FcmSendable {
    String getToken();

    boolean isSent();

    void setSent(boolean sent);

    String getId();
}
