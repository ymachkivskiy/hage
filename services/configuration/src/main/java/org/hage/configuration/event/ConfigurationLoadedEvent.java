package org.hage.configuration.event;


import lombok.Data;
import lombok.ToString;
import org.hage.bus.AgeEvent;
import org.hage.platform.config.ComputationConfiguration;

import javax.annotation.concurrent.Immutable;


@Immutable
@Data
@ToString
public final class ConfigurationLoadedEvent implements AgeEvent {
    private final ComputationConfiguration computationConfiguration;
}
