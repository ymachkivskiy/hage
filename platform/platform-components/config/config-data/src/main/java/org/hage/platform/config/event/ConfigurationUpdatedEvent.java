package org.hage.platform.config.event;


import lombok.Data;
import org.hage.platform.config.Configuration;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public final class ConfigurationUpdatedEvent implements Event {
    private final Configuration configuration;
}
