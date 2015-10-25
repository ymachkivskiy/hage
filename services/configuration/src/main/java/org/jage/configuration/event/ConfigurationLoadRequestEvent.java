package org.jage.configuration.event;

import org.jage.bus.AgeEvent;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConfigurationLoadRequestEvent implements AgeEvent {
    private static final ConfigurationLoadRequestEvent INSTANCE = new ConfigurationLoadRequestEvent();

    private ConfigurationLoadRequestEvent() {
    }

    public static ConfigurationLoadRequestEvent configurationLoadRequest() {
        return INSTANCE;
    }
}
