package org.jage.performance.cluster.communication;

import org.jage.address.node.NodeAddress;
import org.jage.communication.synch.ConversationMessagesAggregator;
import org.jage.performance.cluster.NodeCombinedPerformance;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ClusterNodeAggregator extends ConversationMessagesAggregator<List<NodeCombinedPerformance>, PerformanceServiceMessage> {

    public ClusterNodeAggregator(Set<NodeAddress> expectedMessageSenders) {
        super(expectedMessageSenders);
    }

    @Override
    protected List<NodeCombinedPerformance> aggregate(Collection<PerformanceServiceMessage> messages) {
        return messages
                .stream()
                .map(this::convertMessageToClusterNode)
                .collect(toList());
    }

    private NodeCombinedPerformance convertMessageToClusterNode(PerformanceServiceMessage message) {
        return new NodeCombinedPerformance(message.getHeader().getSender(), message.getPayload());
    }

}
