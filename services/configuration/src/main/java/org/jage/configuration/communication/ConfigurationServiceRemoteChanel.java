package org.jage.configuration.communication;


import lombok.extern.slf4j.Slf4j;
import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.jage.configuration.data.ComputationConfiguration;
import org.jage.configuration.service.ConfigurationService;
import org.jage.platform.component.definition.IComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;

import static org.jage.configuration.communication.ConfigurationMessage.distributeConfigurationMessage;
import static org.jage.configuration.communication.ConfigurationMessage.requestConfigurationMessage;


@Slf4j
public class ConfigurationServiceRemoteChanel extends BaseRemoteChanel<ConfigurationMessage> {


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

    public void distributeConfiguration(ComputationConfiguration configuration) {
        log.debug("Distributing configuration {} ", configuration);
        send(distributeConfigurationMessage(configuration));
    }

    public void acquireConfiguration() {
        log.debug("Acquiring configuration");
        send(requestConfigurationMessage());
    }

    private class RequestConfigurationMessageConsumer extends BaseConditionalMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatches(ConfigurationMessage remoteMessage) {
            return remoteMessage.isRequest();
        }

        @Override
        public void consumeMatchingMessage(ConfigurationMessage configurationMessage) {
            configurationService.distributeConfiguration();
        }
    }

    private class DistributeConfigurationMessageConsumer extends BaseConditionalMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatches(ConfigurationMessage remoteMessage) {
            return remoteMessage.isDistribute();
        }

        @Override
        public void consumeMatchingMessage(ConfigurationMessage configurationMessage) {
            configurationService.updateConfiguration(configurationMessage.getPayload());
        }
    }
}
