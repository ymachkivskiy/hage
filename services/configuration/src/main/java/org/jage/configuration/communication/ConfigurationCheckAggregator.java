package org.jage.configuration.communication;

import org.jage.address.node.NodeAddress;
import org.jage.communication.synch.ConversationMessagesAggregator;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ConfigurationCheckAggregator extends ConversationMessagesAggregator<Set<NodeAddress>, ConfigurationMessage> {

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
