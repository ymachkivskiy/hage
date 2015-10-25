package org.jage.performance.cluster.communication;

import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.communication.api.BaseRemoteChanel;
import org.jage.communication.message.consume.BaseConditionalMessageConsumer;
import org.jage.communication.message.consume.ConversationMessageConsumer;
import org.jage.communication.message.consume.MessageConsumer;
import org.jage.performance.node.NodePerformanceManager;
import org.jage.performance.rate.ClusterNode;
import org.jage.performance.rate.CombinedPerformanceRate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Slf4j
public class PerformanceRemoteChanel extends BaseRemoteChanel<PerformanceServiceMessage> {
    private static final String SERVICE_NAME = "PerformanceService";

    @Autowired
    private NodePerformanceManager nodePerformanceManager;

    protected PerformanceRemoteChanel() {
        super(SERVICE_NAME);
    }

    @Override
    protected void postInit() {
        registerMessageConsumer(new RateRequestedMessageConsumer());
    }

    public CombinedPerformanceRate getNodePerformance(NodeAddress nodeAddress) {
        PerformanceServiceMessage requestMessage = PerformanceServiceMessage.requestPerformanceMessage(getLocalAddress());

        if (nodeAddress.equals(getLocalAddress())) {
            onRemoteMessage(requestMessage);
        }
        return null;
    }

    public List<ClusterNode> getAllNodesPerformances() {

        PerformanceServiceMessage requestMessage = PerformanceServiceMessage.requestPerformanceMessage(getLocalAddress());
        sendMessageToAll(requestMessage);

        return Collections.emptyList();//TODO
    }

    private void sendLocalNodeRateToNode(NodeAddress nodeAddress) {
        log.info("Send local performance to {}", nodeAddress);

        CombinedPerformanceRate localPerformance = nodePerformanceManager.getOverallPerformance();
        PerformanceServiceMessage responseMessage = PerformanceServiceMessage.responsePerformanceMessage(getLocalAddress(), localPerformance);

        if (nodeAddress.equals(getLocalAddress())) {
            onRemoteMessage(responseMessage);
        } else {
            sendMessageToNode(responseMessage, nodeAddress);
        }
    }

    private class RateRequestedMessageConsumer extends BaseConditionalMessageConsumer<PerformanceServiceMessage> {

        @Override
        protected boolean messageMatches(PerformanceServiceMessage remoteMessage) {
            return remoteMessage.isRateRequestedMessage();
        }

        @Override
        protected void consumeMatchingMessage(PerformanceServiceMessage message) {
            sendLocalNodeRateToNode(message.getPayload().getAddress());
        }

    }

}
