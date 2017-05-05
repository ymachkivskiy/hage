package org.hage.platform.cluster.loadbalance.remote;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.OrderedClusterMembersStepView;
import org.hage.platform.cluster.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.cluster.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.hage.platform.cluster.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.hage.platform.cluster.loadbalance.remote.message.MessageUtils;
import org.hage.platform.cluster.loadbalance.remote.response.LoadBalanceMessageResponseProcessor;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.hage.platform.node.executors.simple.WorkerExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@SingletonComponent
public class LoadBalanceEndpoint extends BaseRemoteEndpoint<LoadBalancerRemoteMessage> implements BalanceManager {

    @Autowired
    private LoadBalanceMessageResponseProcessor messageProcessingStrategy;
    @Autowired
    private OrderedClusterMembersStepView orderedClusterMembersStepView;
    @Autowired
    private WorkerExecutor executor;

    protected LoadBalanceEndpoint() {
        super(new ConnectionDescriptor("load-balancer-remote-chanel"), LoadBalancerRemoteMessage.class);
    }

    @Override
    public List<NodeDynamicExecutionInfo> getClusterDynamicStats() {
        return sendToAndAggregateResponses(MessageUtils.requestForStatsMsg(), this::aggregateDynamicStats, new HashSet<>(orderedClusterMembersStepView.getOrderedMembers()));
    }

    @Override
    public void executeBalanceOrders(List<BalanceOrder> orders) {
        List<NodeBalanceOrderTask> balanceOrderTasks = orders
            .stream()
            .map(NodeBalanceOrderTask::new)
            .collect(toList());

        executor.executeAll(balanceOrderTasks);
    }

    @Override
    protected void consumeMessage(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }


    @Override
    protected LoadBalancerRemoteMessage consumeMessageAndRespond(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        return messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }

    private List<NodeDynamicExecutionInfo> aggregateDynamicStats(List<MessageEnvelope<LoadBalancerRemoteMessage>> messageEnvelopes) {
        return messageEnvelopes.stream()
            .map(e -> new NodeDynamicExecutionInfo(e.getOrigin(), e.getBody().getData().getDynamicExecutionInfo()))
            .collect(toList());
    }

    @Data
    private class NodeBalanceOrderTask implements Runnable {
        private final BalanceOrder balanceOrder;

        @Override
        public void run() {
            log.debug("Sending balance order : {}", balanceOrder);
            sendToAndWaitForResponse(MessageUtils.unitsRelocationOrderMsg(balanceOrder.getRelocationOrders()), balanceOrder.getOrderNode());
        }
    }

}
