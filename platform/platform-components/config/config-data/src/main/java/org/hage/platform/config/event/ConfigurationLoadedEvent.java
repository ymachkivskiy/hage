package org.hage.platform.config.event;


import lombok.Data;
import lombok.ToString;
import org.hage.platform.config.Configuration;
import org.hage.platform.util.bus.Event;

import javax.annotation.concurrent.Immutable;


@Immutable
@Data
@ToString
public final class ConfigurationLoadedEvent implements Event {
    private final Configuration configuration;
}
