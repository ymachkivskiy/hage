package org.hage.platform.component.simulationconfig.endpoint.message;


import lombok.Data;
import lombok.Getter;
import org.hage.platform.component.simulationconfig.Configuration;

import java.io.Serializable;

import static lombok.AccessLevel.PACKAGE;


@Data
public class ConfigurationMessage implements Serializable {
    @Getter(PACKAGE)
    private final MessageType messageType;
    private final Configuration configuration;
}
