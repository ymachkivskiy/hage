package org.hage.platform.simconf.event;


import lombok.Data;
import org.hage.platform.simconf.Configuration;
import org.hage.platform.node.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public final class ConfigurationUpdatedEvent implements Event {
    private final Configuration configuration;
}
