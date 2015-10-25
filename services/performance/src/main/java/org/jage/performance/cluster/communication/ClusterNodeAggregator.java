package org.jage.performance.cluster.communication;

import org.jage.address.node.NodeAddress;
import org.jage.communication.synch.ConversationMessagesAggregator;
import org.jage.performance.rate.ClusterNode;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ClusterNodeAggregator extends ConversationMessagesAggregator<List<ClusterNode>, PerformanceServiceMessage> {

    public ClusterNodeAggregator(Set<NodeAddress> expectedMessageSenders) {
        super(expectedMessageSenders);
    }

    @Override
    protected List<ClusterNode> aggregate(Collection<PerformanceServiceMessage> messages) {
        return messages
                .stream()
                .map(this::convertMessageToClusterNode)
                .collect(toList());
    }

    private ClusterNode convertMessageToClusterNode(PerformanceServiceMessage message) {
        return new ClusterNode(message.getHeader().getSender(), message.getPayload());
    }

}
