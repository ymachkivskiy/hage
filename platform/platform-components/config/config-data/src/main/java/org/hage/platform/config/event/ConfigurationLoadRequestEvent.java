package org.hage.platform.config.event;


import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ConfigurationLoadRequestEvent implements Event {
    private static final ConfigurationLoadRequestEvent INSTANCE = new ConfigurationLoadRequestEvent();

    private ConfigurationLoadRequestEvent() {
    }

    public static ConfigurationLoadRequestEvent configurationLoadRequest() {
        return INSTANCE;
    }
}
