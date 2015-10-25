package org.jage.performance.cluster.communication;

import org.jage.address.node.NodeAddress;
import org.jage.communication.message.BaseServiceMessage;
import org.jage.communication.message.ServiceHeaderWithType;
import org.jage.performance.rate.ClusterNode;
import org.jage.performance.rate.CombinedPerformanceRate;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PerformanceServiceMessage extends BaseServiceMessage<ClusterNode> {

    private PerformanceServiceMessage(PerformanceMessageType type, ClusterNode payload) {
        super(ServiceHeaderWithType.create(type), payload);
    }

    public final boolean isRateRequestedMessage() {
        return getType() == PerformanceMessageType.RATE_REQUESTED;
    }

    public final boolean isRateResponseMessage() {
        return getType() == PerformanceMessageType.RATE_RESPONSE;
    }

    private PerformanceMessageType getType() {
        return ((ServiceHeaderWithType<PerformanceMessageType>) super.getHeader()).getType();
    }

    public static PerformanceServiceMessage requestPerformanceMessage(NodeAddress suppliantNodeAddress) {
        return new PerformanceServiceMessage(PerformanceMessageType.RATE_REQUESTED, new ClusterNode(suppliantNodeAddress));
    }

    public static PerformanceServiceMessage responsePerformanceMessage(NodeAddress responderNodeAddress, CombinedPerformanceRate nodePerformanceRate) {
        ClusterNode payload = new ClusterNode(responderNodeAddress);
        payload.setPerformanceRateInfo(nodePerformanceRate);
        return new PerformanceServiceMessage(PerformanceMessageType.RATE_RESPONSE, payload);
    }
}
