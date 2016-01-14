package org.hage.platform.util.bus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseEventBus implements EventBus {

    private final com.google.common.eventbus.EventBus eventBus;

    protected BaseEventBus(com.google.common.eventbus.EventBus bus) {
        eventBus = bus;
    }

    @Override
    public void postEvent(Event event) {
        log.debug("post event {}", event);

        eventBus.post(event);
    }

    @Override
    public void registerSubscriber(EventSubscriber subscriber) {
        log.debug("register subscriber {}", subscriber);

        eventBus.register(subscriber.getEventListener());
    }

    @Override
    public void unregisterSubscriber(EventSubscriber subscriber) {
        log.debug("unregister subscriber {}", subscriber);

        eventBus.unregister(subscriber.getEventListener());
    }
}
