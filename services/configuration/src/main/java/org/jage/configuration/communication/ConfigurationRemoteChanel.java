package org.jage.configuration.communication;


import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.jage.communication.synch.SynchronizationSupport;
import org.jage.configuration.service.ConfigurationStorageService;
import org.jage.platform.config.ComputationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.jage.configuration.communication.ConfigurationMessage.*;


@Slf4j
public class ConfigurationRemoteChanel extends BaseRemoteChanel<ConfigurationMessage> {

    @Autowired
    private ConfigurationStorageService configurationStorageService;

    private final ConfigurationCheckRequestSynchConnector checkSynchConnector = new ConfigurationCheckRequestSynchConnector();

    @Override
    protected void postInit() {
        registerMessageConsumer(new DistributeConfigurationMessageConsumer());
        registerMessageConsumer(new CheckConfigurationMessageConsumer());
        registerMessageConsumer(checkSynchConnector);
    }

    private void checkAndRespondIfConfigurationPresent(ServiceHeader header) {
        log.info("Request for checking if node can participate in computation received from {}", header.getSender());

        ConfigurationMessage responseMessage = configurationStorageService.hasConfiguration()
                ? newRefuseConfigurationMessage(header.getConversationId())
                : newRequestConfigurationMessage(header.getConversationId());

        send(responseMessage, header.getSender());
    }

    public void sendConfigurationToNode(ComputationConfiguration configuration, NodeAddress nodeAddress) {
        log.info("Sending computation configuration {} to node {}", configuration, nodeAddress);
        send(newDistributeConfigurationMessage(configuration), nodeAddress);
    }


    public Set<NodeAddress> getNodesAvailableForComputations() {
        log.info("Querying nodes available for computation");
        return checkSynchConnector.synchronousCall(
                newCheckConfigurationMessage(),
                new ConfigurationCheckAggregator(getAllClusterAddresses())
        );
    }

    private class DistributeConfigurationMessageConsumer extends BaseConditionalMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatches(ConfigurationMessage remoteMessage) {
            return remoteMessage.isDistributeMessage();
        }

        @Override
        public void consumeMatchingMessage(ConfigurationMessage configurationMessage) {
            configurationStorageService.updateConfiguration(configurationMessage.getPayload());
        }
    }

    private class CheckConfigurationMessageConsumer extends BaseConditionalMessageConsumer<ConfigurationMessage> {

        @Override
        protected boolean messageMatches(ConfigurationMessage message) {
            return message.isCheckMessage();
        }

        @Override
        protected void consumeMatchingMessage(ConfigurationMessage message) {
            checkAndRespondIfConfigurationPresent(message.getHeader());
        }
    }

    private class ConfigurationCheckRequestSynchConnector extends SynchronizationSupport<Set<NodeAddress>, ConfigurationMessage> {

        public ConfigurationCheckRequestSynchConnector() {
            super(ConfigurationRemoteChanel.this::nextConversationId);
        }

        @Override
        protected void sendMessage(ConfigurationMessage message) {
            send(message);
        }

        @Override
        protected boolean messageMatches(ConfigurationMessage message) {
            return message.isRefuseMessage() || message.isRequestMessage();
        }
    }
}
