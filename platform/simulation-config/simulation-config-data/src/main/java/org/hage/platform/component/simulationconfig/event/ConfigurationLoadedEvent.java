package org.hage.platform.component.simulationconfig.event;


import lombok.Data;
import lombok.ToString;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.node.bus.Event;

import javax.annotation.concurrent.Immutable;


@Immutable
@Data
@ToString
public final class ConfigurationLoadedEvent implements Event {
    private final Configuration configuration;
}
