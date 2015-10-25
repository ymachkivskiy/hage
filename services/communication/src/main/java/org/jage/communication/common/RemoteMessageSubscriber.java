package org.jage.communication.common;


import java.io.Serializable;


public interface RemoteMessageSubscriber<E extends Serializable> {
    void onRemoteMessage(E message);
}
