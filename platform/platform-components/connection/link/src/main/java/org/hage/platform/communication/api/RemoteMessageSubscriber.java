package org.hage.platform.communication.api;


import java.io.Serializable;


public interface RemoteMessageSubscriber<E extends Serializable> {
    void onRemoteMessage(E message);
}
