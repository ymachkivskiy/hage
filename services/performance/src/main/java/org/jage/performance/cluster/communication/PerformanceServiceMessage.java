package org.jage.performance.cluster.communication;

import org.jage.address.node.NodeAddress;
import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.ServiceMessage;
import org.jage.performance.rate.CombinedPerformanceRate;

import javax.annotation.concurrent.Immutable;

import static org.jage.communication.message.service.ServiceHeader.create;

@Immutable
public class PerformanceServiceMessage extends ServiceMessage<CombinedPerformanceRate> {

    private PerformanceServiceMessage(ServiceHeader header, CombinedPerformanceRate payload) {
        super(header, payload);
    }

    public final boolean isRateRequestedMessage() {
        return getType() == PerformanceMessageType.RATE_REQUESTED;
    }

    public final boolean isRateResponseMessage() {
        return getType() == PerformanceMessageType.RATE_RESPONSE;
    }

    private PerformanceMessageType getType() {
        return ((ServiceHeader<PerformanceMessageType>) super.getHeader()).getType();
    }

    public static PerformanceServiceMessage requestPerformanceMessage(NodeAddress suppliantNodeAddress) {
        ServiceHeader header = create(PerformanceMessageType.RATE_REQUESTED, suppliantNodeAddress);
        return new PerformanceServiceMessage(header, null);
    }

    public static PerformanceServiceMessage responsePerformanceMessage(Long conversationId, NodeAddress responderNodeAddress, CombinedPerformanceRate nodePerformanceRate) {
        ServiceHeader header = create(PerformanceMessageType.RATE_RESPONSE, conversationId, responderNodeAddress);
        return new PerformanceServiceMessage(header, nodePerformanceRate);
    }
}
