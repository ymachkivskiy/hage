package org.jage.performance.cluster.communication;

import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.jage.communication.synch.SynchronizationSupport;
import org.jage.performance.node.NodePerformanceManager;
import org.jage.performance.rate.ClusterNode;
import org.jage.performance.rate.CombinedPerformanceRate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.jage.performance.cluster.communication.PerformanceServiceMessage.newRequestPerformanceMessage;
import static org.jage.performance.cluster.communication.PerformanceServiceMessage.newResponsePerformanceMessage;

@Slf4j
public class PerformanceRemoteChanel extends BaseRemoteChanel<PerformanceServiceMessage> {

    @Autowired
    private NodePerformanceManager nodePerformanceManager;

    private final PerformanceRequestSynchConnector performanceRequestSynchConnector = new PerformanceRequestSynchConnector();

    @Override
    protected void postInit() {
        registerMessageConsumer(new RateRequestedMessageConsumerConnector());
        registerMessageConsumer(performanceRequestSynchConnector);
    }

    public List<ClusterNode> getAllNodesPerformances() {
        return getNodesPerformances(getAllClusterAddresses());
    }

    public List<ClusterNode> getNodesPerformances(Set<NodeAddress> nodeAddresses) {
        log.info("Request for {} nodes performances", nodeAddresses);

        return performanceRequestSynchConnector.synchronousCall(
                newRequestPerformanceMessage(),
                new ClusterNodeAggregator(nodeAddresses)
        );
    }

    private void sendLocalNodeRateToNode(ServiceHeader requestMessageHeader) {
        log.info("Send local performance request for {}", requestMessageHeader);

        CombinedPerformanceRate localPerformance = nodePerformanceManager.getOverallPerformance();
        PerformanceServiceMessage responseMessage = newResponsePerformanceMessage(requestMessageHeader.getConversationId(), localPerformance);

        send(responseMessage, requestMessageHeader.getSender());
    }

    private class RateRequestedMessageConsumerConnector extends BaseConditionalMessageConsumer<PerformanceServiceMessage> {

        @Override
        protected boolean messageMatches(PerformanceServiceMessage remoteMessage) {
            return remoteMessage.isRateRequestedMessage();
        }

        @Override
        protected void consumeMatchingMessage(PerformanceServiceMessage message) {
            sendLocalNodeRateToNode(message.getHeader());
        }

    }

    private class PerformanceRequestSynchConnector extends SynchronizationSupport<List<ClusterNode>, PerformanceServiceMessage> {

        public PerformanceRequestSynchConnector() {
            super(PerformanceRemoteChanel.this::nextConversationId);
        }

        @Override
        protected void sendMessage(PerformanceServiceMessage message) {
            send(message);
        }

        @Override
        protected boolean messageMatches(PerformanceServiceMessage message) {
            return message.isRateResponseMessage();
        }
    }

}
