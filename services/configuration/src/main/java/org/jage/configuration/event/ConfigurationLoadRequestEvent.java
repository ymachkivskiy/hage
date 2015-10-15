package org.jage.configuration.event;

import org.jage.bus.AgeEvent;

public class ConfigurationLoadRequestEvent implements AgeEvent {
    public static final ConfigurationLoadRequestEvent INSTANCE = new ConfigurationLoadRequestEvent();

    private ConfigurationLoadRequestEvent() {
    }

}
