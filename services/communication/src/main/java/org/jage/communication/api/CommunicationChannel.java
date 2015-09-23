package org.jage.communication.api;


public interface CommunicationChannel<T> {

    public void publish(final T message);

    public void subscribe(MessageSubscriber<T> subscriber);

    public void unsubscribe(MessageSubscriber<T> subscriber);
}
