package org.hage.platform.component.loadbalance.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.OrderedClusterMembersStepView;
import org.hage.platform.component.loadbalance.precondition.NodeDynamicStats;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.hage.platform.component.loadbalance.remote.message.MessageUtils;
import org.hage.platform.component.loadbalance.remote.response.LoadBalanceMessageResponseProcessor;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
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

    protected LoadBalanceEndpoint() {
        super(new ConnectionDescriptor("load-balancer-remote-chanel"), LoadBalancerRemoteMessage.class);
    }

    @Override
    public List<NodeDynamicStats> getClusterDynamicStats() {
        return sendToAndAggregateResponses(MessageUtils.requestForStatsMsg(), this::aggregateDynamicStats, new HashSet<>(orderedClusterMembersStepView.getOrderedMembers()));
    }

    @Override
    public void executeBalanceOrders(List<BalanceOrder> orders) {
        //todo : NOT IMPLEMENTED

    }

    @Override
    protected void consumeMessage(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }


    @Override
    protected LoadBalancerRemoteMessage consumeMessageAndRespond(MessageEnvelope<LoadBalancerRemoteMessage> envelope) {
        return messageProcessingStrategy.processAndAnswer(envelope.getBody());
    }

    private List<NodeDynamicStats> aggregateDynamicStats(List<MessageEnvelope<LoadBalancerRemoteMessage>> messageEnvelopes) {
        return messageEnvelopes.stream()
            .map(e -> new NodeDynamicStats(e.getOrigin(), e.getBody().getData().getDynamicStats()))
            .collect(toList());
    }

}
