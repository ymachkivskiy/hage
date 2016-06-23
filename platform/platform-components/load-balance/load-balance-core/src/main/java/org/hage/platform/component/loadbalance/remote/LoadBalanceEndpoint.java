package org.hage.platform.component.loadbalance.remote;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.OrderedClusterMembersStepView;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.hage.platform.component.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.hage.platform.component.loadbalance.remote.response.LoadBalanceMessageResponseProcessor;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.hage.platform.util.executors.simple.WorkerExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.loadbalance.remote.message.MessageUtils.requestForStatsMsg;
import static org.hage.platform.component.loadbalance.remote.message.MessageUtils.unitsRelocationOrderMsg;

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
        return sendToAndAggregateResponses(requestForStatsMsg(), this::aggregateDynamicStats, new HashSet<>(orderedClusterMembersStepView.getOrderedMembers()));
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
            log.debug("Sending order");
            sendToAndWaitForResponse(unitsRelocationOrderMsg(balanceOrder.getRelocationOrders()), balanceOrder.getOrderNode());
        }
    }

}
