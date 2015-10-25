package org.jage.configuration.communication;


import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.ServiceMessage;
import org.jage.platform.config.ComputationConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;


@Immutable
public final class ConfigurationMessage extends ServiceMessage<ComputationConfiguration> {


    public static final ConfigurationMessage REQUEST_CONFIGURATION_MESSAGE = new ConfigurationMessage(ConfigurationMessageType.REQUEST, null);

    public ConfigurationMessage(ConfigurationMessageType type, ComputationConfiguration computationConfiguration) {
        super(ServiceHeader.create(type), computationConfiguration);
    }

    @Nonnull
    public static ConfigurationMessage distributeConfigurationMessage(ComputationConfiguration configuration) {
        return new ConfigurationMessage(ConfigurationMessageType.DISTRIBUTE, configuration);
    }

    @Nonnull
    public static ConfigurationMessage requestConfigurationMessage() {
        return REQUEST_CONFIGURATION_MESSAGE;
    }

    public boolean isRequest() {
        return getHeader().getType() == ConfigurationMessageType.REQUEST;
    }

    public boolean isDistribute() {
        return getHeader().getType() == ConfigurationMessageType.DISTRIBUTE;
    }

}
