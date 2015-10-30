package org.jage.performance.cluster.communication;

import org.jage.communication.message.service.ServiceHeader;
import org.jage.communication.message.service.ServiceMessage;
import org.jage.performance.node.measure.PerformanceRate;

import javax.annotation.concurrent.Immutable;

import static org.jage.communication.message.service.ServiceHeader.create;

@Immutable
public class PerformanceServiceMessage extends ServiceMessage<PerformanceMessageType, PerformanceRate> {

    private PerformanceServiceMessage(ServiceHeader<PerformanceMessageType> header, PerformanceRate payload) {
        super(header, payload);
    }

    public final boolean isRateRequestedMessage() {
        return getHeader().getType() == PerformanceMessageType.RATE_REQUESTED;
    }

    public final boolean isRateResponseMessage() {
        return getHeader().getType() == PerformanceMessageType.RATE_RESPONSE;
    }

    public static PerformanceServiceMessage newRequestPerformanceMessage() {
        return new PerformanceServiceMessage(create(PerformanceMessageType.RATE_REQUESTED), null);
    }

    public static PerformanceServiceMessage newResponsePerformanceMessage(Long conversationId, PerformanceRate nodePerformanceRate) {
        return new PerformanceServiceMessage(create(PerformanceMessageType.RATE_RESPONSE, conversationId), nodePerformanceRate);
    }
}
