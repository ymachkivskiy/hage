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

import static org.jage.performance.cluster.communication.PerformanceServiceMessage.requestPerformanceMessage;
import static org.jage.performance.cluster.communication.PerformanceServiceMessage.responsePerformanceMessage;

@Slf4j
public class PerformanceRemoteChanel extends BaseRemoteChanel<PerformanceServiceMessage> {
    private static final String SERVICE_NAME = "PerformanceService";

    @Autowired
    private NodePerformanceManager nodePerformanceManager;

    private final PerformanceRequestSynchConnector performanceRequestSynchConnector;

    protected PerformanceRemoteChanel() {
        super(SERVICE_NAME);
        this.performanceRequestSynchConnector = new PerformanceRequestSynchConnector();
    }

    @Override
    protected void postInit() {
        registerMessageConsumer(new RateRequestedMessageConsumerConnector());
        registerMessageConsumer(performanceRequestSynchConnector);
    }

    public List<ClusterNode> getAllNodesPerformances() {
        log.info("Request for all nodes performances");

        return performanceRequestSynchConnector.synchronousCall(
                requestPerformanceMessage(getLocalAddress()),
                new ClusterNodeAggregator(getAllClusterAddresses())
        );
    }

    private void sendLocalNodeRateToNode(ServiceHeader requestMessageHeader) {
        log.info("Send local performance request for {}", requestMessageHeader);

        CombinedPerformanceRate localPerformance = nodePerformanceManager.getOverallPerformance();
        PerformanceServiceMessage responseMessage = responsePerformanceMessage(requestMessageHeader.getConversationId(), getLocalAddress(), localPerformance);

        NodeAddress performanceRequestSender = requestMessageHeader.getSender();

        if (performanceRequestSender.equals(getLocalAddress())) {
            onRemoteMessage(responseMessage);
        } else {
            send(responseMessage, performanceRequestSender);
        }
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
