package org.hage.platform.node.bus;

public interface EventBus {
    void post(Event event);

    void register(EventSubscriber subscriber);

    void unregister(EventSubscriber subscriber);
}
