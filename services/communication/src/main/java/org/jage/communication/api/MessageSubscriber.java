package org.jage.communication.api;


import javax.annotation.Nonnull;


public interface MessageSubscriber<E> {

    void onMessage(@Nonnull E message);
}
