package org.jage.configuration.communication;


import lombok.extern.slf4j.Slf4j;
import org.jage.communication.common.BaseRemoteChanel;
import org.jage.communication.common.RemoteMessageConsumer;
import org.jage.configuration.communication.ConfigurationMessage.MessageType;
import org.jage.configuration.service.ConfigurationService;
import org.jage.platform.component.definition.IComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


@Slf4j
public class ConfigurationServiceRemoteChanel
        extends BaseRemoteChanel<ConfigurationMessage> {


    public static final String SERVICE_NAME = "ConfigurationPropagationHub";

    @Autowired
    private ConfigurationService configurationService;

    protected ConfigurationServiceRemoteChanel() {
        super(SERVICE_NAME);
    }


    @Override
    protected void postInit() {
        registerMessageConsumer(new RequestConfigurationMessageConsumer());
        registerMessageConsumer(new DistributeConfigurationMessageConsumer());
    }

    public void distributeConfiguration(Collection<IComponentDefinition> configuration) {
        log.debug("Distributing configuration {} ", configuration);

        ArrayList<IComponentDefinition> definitions = new ArrayList<>(configuration);
        ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.DISTRIBUTE, definitions);

        sendMessageToAll(message);
    }

    public void acquireConfiguration() {
        log.debug("Acquiring configuration");

        ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.REQUEST);

        sendMessageToAll(message);
    }

    private class RequestConfigurationMessageConsumer extends RemoteMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatch(ConfigurationMessage remoteMessage) {
            return remoteMessage.getType() == MessageType.REQUEST;
        }

        @Override
        public void accept(ConfigurationMessage configurationMessage) {
            configurationService.distributeConfiguration();
        }
    }

    private class DistributeConfigurationMessageConsumer extends RemoteMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatch(ConfigurationMessage remoteMessage) {
            return remoteMessage.getType() == MessageType.DISTRIBUTE;
        }

        @Override
        public void accept(ConfigurationMessage configurationMessage) {
            Serializable payload = configurationMessage.getPayload();
            if (!(payload instanceof Collection)) {
                throw new NullPointerException(String.format("Configuration payload was null. Faulty message was sent by %s.", configurationMessage));
            }
            configurationService.updateConfiguration((Collection<IComponentDefinition>) payload);
        }
    }
}
