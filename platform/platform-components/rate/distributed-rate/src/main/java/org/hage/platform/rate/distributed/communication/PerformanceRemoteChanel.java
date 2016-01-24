package org.hage.platform.rate.distributed.communication;

import lombok.extern.slf4j.Slf4j;
import org.hage.address.node.NodeAddress;
import org.hage.communication.api.BaseRemoteChanel;
import org.hage.communication.message.service.ServiceHeader;
import org.hage.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.hage.communication.synch.SynchronizationSupport;
import org.hage.platform.rate.distributed.NodeCombinedPerformance;
import org.hage.platform.rate.local.NodePerformanceManager;
import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

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

    public List<NodeCombinedPerformance> getAllNodesPerformances() {
        return getNodesPerformances(getAllClusterAddresses());
    }

    public List<NodeCombinedPerformance> getNodesPerformances(Set<NodeAddress> nodeAddresses) {
        log.info("Request for {} nodes performances", nodeAddresses);

        return performanceRequestSynchConnector.synchronousCall(
                PerformanceServiceMessage.newRequestPerformanceMessage(),
                new ClusterNodeAggregator(nodeAddresses)
        );
    }

    private void sendLocalNodeRateToNode(ServiceHeader requestMessageHeader) {
        log.info("Send local performance request for {}", requestMessageHeader);

        PerformanceRate localPerformance = nodePerformanceManager.getOverallPerformance();
        PerformanceServiceMessage responseMessage = PerformanceServiceMessage.newResponsePerformanceMessage(requestMessageHeader.getConversationId(), localPerformance);

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

    private class PerformanceRequestSynchConnector extends SynchronizationSupport<List<NodeCombinedPerformance>, PerformanceServiceMessage> {

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
