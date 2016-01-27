package org.hage.platform.config.distributed.link;

import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.synch.ConversationMessagesAggregator;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

class ConfigurationCheckAggregator extends ConversationMessagesAggregator<Set<NodeAddress>, ConfigurationMessage> {

    public ConfigurationCheckAggregator(Collection<NodeAddress> nodeAddresses) {
        super(nodeAddresses);
    }

    @Override
    protected Set<NodeAddress> aggregate(Collection<ConfigurationMessage> messages) {
        return messages
                .stream()
                .filter(ConfigurationMessage::isRequestMessage)
                .map(m -> m.getHeader().getSender())
                .collect(toSet());
    }

}
