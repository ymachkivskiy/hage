package org.hage.platform.rate.distributed.communication;

import org.hage.platform.rate.distributed.NodeCombinedPerformance;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.synch.ConversationMessagesAggregator;

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
