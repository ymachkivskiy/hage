package org.hage.platform.config.distribution.endpoint.message;


import lombok.Data;
import lombok.Getter;
import org.hage.platform.config.ComputationConfiguration;

import java.io.Serializable;

import static lombok.AccessLevel.PACKAGE;


@Data
public class ConfigurationMessage implements Serializable {
    @Getter(PACKAGE)
    private final MessageType messageType;
    private final ComputationConfiguration configuration;
}
