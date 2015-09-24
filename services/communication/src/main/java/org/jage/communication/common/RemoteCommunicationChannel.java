package org.jage.communication.common;


import java.io.Serializable;


public interface RemoteCommunicationChannel<T extends Serializable> {

    public void publish(final T message);

    public void subscribe(RemoteMessageSubscriber<T> subscriber);

    public void unsubscribe(RemoteMessageSubscriber<T> subscriber);
}
