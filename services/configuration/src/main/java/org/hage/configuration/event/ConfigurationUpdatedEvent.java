package org.hage.configuration.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;

@Immutable
@AllArgsConstructor
@ToString
public final class ConfigurationUpdatedEvent implements Event {

    @Getter
    private final ComputationConfiguration computationConfiguration;

}
