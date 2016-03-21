package org.hage.platform.config.distribution.endpoint;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.config.ConfigurationStorage;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.distribution.division.Allocation;
import org.hage.platform.config.distribution.endpoint.message.ConfigurationMessage;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.hage.platform.config.distribution.endpoint.message.MessageUtils.*;


@Slf4j
@Component
public class ConfigurationEndpoint extends BaseRemoteEndpoint<ConfigurationMessage> {

    private static final String CHANEL_NAME = "configuration-remote-chanel";

    @Autowired
    private ConfigurationStorage configurationStorageService;

    public ConfigurationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), ConfigurationMessage.class);
    }

    public void distribute(Allocation allocation) {
        log.debug("Distribute allocationPart: '{}'", allocation);
        allocation.getAllocationParts().forEach(allPart -> sendAllocation(allPart.getConfig(), allPart.getAddress()));
    }

    public Set<NodeAddress> getNodesAvailableForComputations() {
        log.debug("Querying nodes available for computation");
        return sendToAllAndAggregateResponse(checkNeedConfigMsg(), new ConfigurationRequestNodeAddressesAggregator());
    }

    public void sendAllocation(Configuration configuration, NodeAddress address) {
        log.debug("Sending computation configuration part '{}' to '{}'", configuration, address);
        sendTo(distributeMsg(configuration), address);
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
}
