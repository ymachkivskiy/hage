package org.hage.platform.component.rate.remote;

import lombok.Data;
import org.hage.platform.component.rate.cluster.PerformanceRate;
import org.hage.platform.component.rate.model.ComputationRatingConfig;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;


@Data
@Immutable
class PerformanceRemoteMessage implements Serializable {
    private final MessageType type;
    private final ComputationRatingConfig ratingConfig;
    private final PerformanceRate performanceRate;

    private PerformanceRemoteMessage(MessageType type, ComputationRatingConfig ratingConfig, PerformanceRate performanceRate) {
        this.type = type;
        this.ratingConfig = ratingConfig;
        this.performanceRate = performanceRate;
    }

    public static PerformanceRemoteMessage rateRequestMessage(ComputationRatingConfig ratingConfig) {
        return new PerformanceRemoteMessage(MessageType.REQUEST, ratingConfig, null);
    }

    public static PerformanceRemoteMessage rateResponseMessage(PerformanceRate performanceRate) {
        return new PerformanceRemoteMessage(MessageType.RESPONSE, null, performanceRate);
    }

    private enum MessageType {
        REQUEST, RESPONSE
    }
}
