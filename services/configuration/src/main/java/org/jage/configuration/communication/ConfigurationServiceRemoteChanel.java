package org.jage.configuration.communication;


import lombok.extern.slf4j.Slf4j;
import org.jage.communication.common.AbstractRemoteChanel;
import org.jage.configuration.communication.ConfigurationMessage.MessageType;
import org.jage.configuration.service.ConfigurationService;
import org.jage.platform.component.definition.IComponentDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


@Slf4j
public class ConfigurationServiceRemoteChanel
        extends AbstractRemoteChanel<ConfigurationMessage> {


    public static final String SERVICE_NAME = "ConfigurationPropagationHub";

    @Autowired
    private ConfigurationService configurationService;

    protected ConfigurationServiceRemoteChanel() {
        super(SERVICE_NAME);
    }


    @Override
    protected void postInit() {
        registerConsumerHandler(message -> message.getType() == MessageType.REQUEST,
                                message -> configurationService.distributeConfiguration()
        );

        registerConsumerHandler(message -> message.getType() == MessageType.DISTRIBUTE,
                                message -> {
                                    final Serializable payload = message.getPayload();
                                    if(!(payload instanceof Collection)) {
                                        throw new NullPointerException(String.format("Configuration payload was null. Faulty message was sent by %s.", message));
                                    }
                                    configurationService.updateConfiguration((Collection<IComponentDefinition>) payload);
                                }
        );

    }

    public void distributeConfiguration(Collection<IComponentDefinition> configuration) {
        log.debug("Distributing configuration {} ", configuration);

        ArrayList<IComponentDefinition> definitions = new ArrayList<>(configuration);
        ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.DISTRIBUTE, definitions);

        sendMessage(message);
    }

    public void acquireConfiguration() {
        log.debug("Acquiring configuration");

        ConfigurationMessage message = ConfigurationMessage.create(ConfigurationMessage.MessageType.REQUEST);

        sendMessage(message);
    }
}
