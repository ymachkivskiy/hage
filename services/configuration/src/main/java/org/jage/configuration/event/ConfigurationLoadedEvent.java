package org.jage.configuration.event;


import lombok.Data;
import lombok.ToString;
import org.jage.bus.AgeEvent;
import org.jage.platform.config.ComputationConfiguration;

import javax.annotation.concurrent.Immutable;


@Immutable
@Data
@ToString
public final class ConfigurationLoadedEvent implements AgeEvent {
    private final ComputationConfiguration computationConfiguration;
}
