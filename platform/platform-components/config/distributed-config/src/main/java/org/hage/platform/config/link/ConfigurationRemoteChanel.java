package org.hage.platform.config.link;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.node.NodeAddress;
import org.hage.platform.communication.api.BaseRemoteChanel;
import org.hage.platform.communication.message.service.ServiceHeader;
import org.hage.platform.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.hage.platform.communication.synch.SynchronizationSupport;
import org.hage.platform.config.ConfigurationAllocation;
import org.hage.platform.config.ConfigurationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Slf4j
@Component
public class ConfigurationRemoteChanel extends BaseRemoteChanel<ConfigurationMessage> {

    @Autowired
    private ConfigurationStorage configurationStorageService;

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
                ? ConfigurationMessage.newRefuseConfigurationMessage(header.getConversationId())
                : ConfigurationMessage.newRequestConfigurationMessage(header.getConversationId());

        send(responseMessage, header.getSender());
    }

    public void sendConfiguration(ConfigurationAllocation configurationAllocation) {
        log.info("Sending computation configuration part {}", configurationAllocation);

        ConfigurationMessage message = ConfigurationMessage.newDistributeConfigurationMessage(configurationAllocation.getConfiguration());
        NodeAddress destinationNode = configurationAllocation.getDestinationNode();

        send(message, destinationNode);
    }


    public Set<NodeAddress> getNodesAvailableForComputations() {
        log.info("Querying nodes available for computation");
        return checkSynchConnector.synchronousCall(
                ConfigurationMessage.newCheckConfigurationMessage(),
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
