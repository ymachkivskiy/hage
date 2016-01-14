package org.hage.platform.util.bus;

public interface EventBus {
    void postEvent(Event event);

    void registerSubscriber(EventSubscriber subscriber);

    void unregisterSubscriber(EventSubscriber subscriber);
}
