package org.hage.configuration.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hage.bus.AgeEvent;
import org.hage.platform.config.ComputationConfiguration;

import javax.annotation.concurrent.Immutable;

@Immutable
@AllArgsConstructor
@ToString
public final class ConfigurationUpdatedEvent implements AgeEvent {

    @Getter
    private final ComputationConfiguration computationConfiguration;

}
