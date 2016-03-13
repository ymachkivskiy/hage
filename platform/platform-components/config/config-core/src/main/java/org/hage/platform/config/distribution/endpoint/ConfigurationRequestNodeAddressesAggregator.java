package org.hage.platform.config.distribution.endpoint;


import org.hage.platform.config.distribution.endpoint.message.ConfigurationMessage;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.remote.endpoint.MessageAggregator;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hage.platform.config.distribution.endpoint.message.MessageUtils.isRequestMsg;

class ConfigurationRequestNodeAddressesAggregator implements MessageAggregator<ConfigurationMessage, Set<NodeAddress>> {

    @Override
    public Set<NodeAddress> aggregate(Collection<MessageEnvelope<ConfigurationMessage>> envelopes) {
        return envelopes
            .stream()
            .filter(envelope -> isRequestMsg(envelope.getBody()))
            .map(MessageEnvelope::getOrigin)
            .collect(toSet());
    }

}
