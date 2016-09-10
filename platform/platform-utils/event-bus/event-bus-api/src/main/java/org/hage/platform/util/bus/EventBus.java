package org.hage.platform.util.bus;

public interface EventBus {
    void post(Event event);

    void register(EventSubscriber subscriber);

    void unregister(EventSubscriber subscriber);
}
