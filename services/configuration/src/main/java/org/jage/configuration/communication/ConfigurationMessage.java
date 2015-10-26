package org.jage.configuration.communication;


import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.ServiceMessage;
import org.jage.platform.config.ComputationConfiguration;

import javax.annotation.concurrent.Immutable;

import static org.jage.communication.message.service.ServiceHeader.create;


@Immutable
public final class ConfigurationMessage extends ServiceMessage<ConfigurationMessageType, ComputationConfiguration> {

    private ConfigurationMessage(ServiceHeader<ConfigurationMessageType> header, ComputationConfiguration computationConfiguration) {
        super(header, computationConfiguration);
    }

    public static ConfigurationMessage newDistributeConfigurationMessage(ComputationConfiguration configuration) {
        return new ConfigurationMessage(create(ConfigurationMessageType.DISTRIBUTE), configuration);
    }

    public static ConfigurationMessage newRequestConfigurationMessage(Long conversationId) {
        return new ConfigurationMessage(create(ConfigurationMessageType.REQUEST, conversationId), null);
    }

    public static ConfigurationMessage newCheckConfigurationMessage() {
        return new ConfigurationMessage(create(ConfigurationMessageType.CHECK), null);
    }

    public static ConfigurationMessage newRefuseConfigurationMessage(Long conversationId) {
        return new ConfigurationMessage(create(ConfigurationMessageType.REFUSE, conversationId), null);
    }

    public boolean isCheckMessage() {
        return getHeader().getType() == ConfigurationMessageType.CHECK;
    }

    public boolean isRequestMessage() {
        return getHeader().getType() == ConfigurationMessageType.REQUEST;
    }

    public boolean isDistributeMessage() {
        return getHeader().getType() == ConfigurationMessageType.DISTRIBUTE;
    }

    public boolean isRefuseMessage() {
        return getHeader().getType() == ConfigurationMessageType.REFUSE;
    }
}
