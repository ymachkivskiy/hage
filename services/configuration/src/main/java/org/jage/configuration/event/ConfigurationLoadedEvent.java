package org.jage.configuration.event;


import lombok.Data;
import lombok.ToString;
import org.jage.bus.AgeEvent;
import org.jage.configuration.data.ComputationConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;


@Immutable
@Data
@ToString
public class ConfigurationLoadedEvent implements AgeEvent {

    @Nonnull
    private final ComputationConfiguration computationConfiguration;
}
