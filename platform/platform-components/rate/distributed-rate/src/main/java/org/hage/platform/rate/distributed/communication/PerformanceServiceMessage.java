package org.hage.platform.rate.distributed.communication;

import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.hage.platform.util.communication.message.service.ServiceHeader;
import org.hage.platform.util.communication.message.service.ServiceMessage;

import javax.annotation.concurrent.Immutable;

import static org.hage.platform.util.communication.message.service.ServiceHeader.create;

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
