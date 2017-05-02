package org.hage.platform.component.simulationconfig.event;


import lombok.Data;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@Data
public final class ConfigurationUpdatedEvent implements Event {
    private final Configuration configuration;
}
