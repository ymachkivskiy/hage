package org.hage.platform.component.rate.cluster.communication;

import org.hage.communication.message.service.ServiceHeader;
import org.hage.communication.message.service.ServiceMessage;
import org.hage.platform.component.rate.node.measure.PerformanceRate;

import javax.annotation.concurrent.Immutable;

import static org.hage.communication.message.service.ServiceHeader.create;

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
