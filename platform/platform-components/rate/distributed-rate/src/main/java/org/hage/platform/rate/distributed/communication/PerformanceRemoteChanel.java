package org.hage.platform.rate.distributed.communication;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.distributed.NodeCombinedPerformance;
import org.hage.platform.rate.local.LocalPerformanceManager;
import org.hage.platform.rate.local.measure.PerformanceRate;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.api.BaseRemoteChanel;
import org.hage.platform.util.communication.message.service.ServiceHeader;
import org.hage.platform.util.communication.message.service.consume.BaseConditionalMessageConsumer;
import org.hage.platform.util.communication.synch.SynchronizationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class PerformanceRemoteChanel extends BaseRemoteChanel<PerformanceServiceMessage> {

    @Autowired
    private LocalPerformanceManager localPerformanceManager;

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

        PerformanceRate localPerformance = localPerformanceManager.getLocalPerformanceRate();
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
