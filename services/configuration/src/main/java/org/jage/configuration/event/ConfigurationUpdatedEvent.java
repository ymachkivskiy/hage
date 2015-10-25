package org.jage.configuration.event;


import lombok.Data;
import org.jage.bus.AgeEvent;
import org.jage.configuration.data.ComputationConfiguration;

import javax.annotation.Nonnull;


@Data
public class ConfigurationUpdatedEvent implements AgeEvent {

    @Nonnull
    private final ComputationConfiguration computationConfiguration;

}
