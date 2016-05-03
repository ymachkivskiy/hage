package org.hage.platform.component.simulationconfig.endpoint;


import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.simulationconfig.endpoint.message.ConfigurationMessage;
import org.hage.platform.util.connection.remote.endpoint.MessageAggregator;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hage.platform.component.simulationconfig.endpoint.message.MessageUtils.isRequestMsg;

class ConfigurationRequestNodeAddressesAggregator implements MessageAggregator<ConfigurationMessage, Set<NodeAddress>> {

    @Override
    public Set<NodeAddress> aggregate(List<MessageEnvelope<ConfigurationMessage>> envelopes) {
        return envelopes
            .stream()
            .filter(envelope -> isRequestMsg(envelope.getBody()))
            .map(MessageEnvelope::getOrigin)
            .collect(toSet());
    }

}
