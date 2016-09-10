package org.hage.platform.component.simulationconfig.endpoint;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.ConfigurationStorage;
import org.hage.platform.component.simulationconfig.division.Allocation;
import org.hage.platform.component.simulationconfig.endpoint.message.ConfigurationMessage;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hage.platform.component.simulationconfig.endpoint.message.MessageUtils.*;


@Slf4j
@SingletonComponent
class ConfigurationEndpoint extends BaseRemoteEndpoint<ConfigurationMessage> implements ClusterSimulationConfigurator {

    private static final String CHANEL_NAME = "configuration-remote-chanel";

    @Autowired
    private ConfigurationStorage configurationStorageService;

    public ConfigurationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), ConfigurationMessage.class);
    }

    @Override
    public void distribute(Allocation allocation) {
        log.debug("Distribute allocationPart: '{}'", allocation);
        allocation.getAllocationParts().forEach(allPart -> sendAllocation(allPart.getConfig(), allPart.getAddress()));
    }

    @Override
    public Set<NodeAddress> getNodesAvailableForComputations() {
        log.debug("Querying nodes available for computation");
        return sendToAllAndAggregateResponse(checkNeedConfigMsg(), this::aggregateRequestMessages);
    }

    @Override
    protected ConfigurationMessage consumeMessageAndRespond(MessageEnvelope<ConfigurationMessage> envelope) {
        log.debug("Got envelope which requires response: '{}'", envelope);

        if (isCheckMsg(envelope.getBody())) {
            log.debug("Request for check if this node will accept configuration");

            return configurationStorageService.hasConfiguration()
                ? refuseMsg()
                : requestConfigMsg();
        } else {
            log.error("Got message which requires response '{}', but response for this type of message is not supported", envelope.getBody());
            throw new UnsupportedOperationException("Respond to other than check not supported");
        }
    }

    @Override
    protected void consumeMessage(MessageEnvelope<ConfigurationMessage> envelope) {
        log.debug("Consume message '{}'", envelope);

        if (isDistributeMsg(envelope.getBody())) {
            configurationStorageService.updateConfiguration(envelope.getBody().getConfiguration());
        }
    }

    private void sendAllocation(Configuration configuration, NodeAddress address) {
        log.debug("Sending computation configuration part '{}' to '{}'", configuration, address);
        sendTo(distributeMsg(configuration), address);
    }

    private Set<NodeAddress> aggregateRequestMessages(List<MessageEnvelope<ConfigurationMessage>> envelopes) {
        return envelopes
            .stream()
            .filter(envelope -> isRequestMsg(envelope.getBody()))
            .map(MessageEnvelope::getOrigin)
            .collect(toSet());
    }
}
