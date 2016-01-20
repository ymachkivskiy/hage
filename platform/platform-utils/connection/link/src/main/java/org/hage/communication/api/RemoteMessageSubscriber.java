package org.hage.communication.api;


import java.io.Serializable;


public interface RemoteMessageSubscriber<E extends Serializable> {
    void onRemoteMessage(E message);
}
