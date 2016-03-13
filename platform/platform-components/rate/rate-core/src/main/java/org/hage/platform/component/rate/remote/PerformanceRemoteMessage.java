package org.hage.platform.component.rate.remote;

import lombok.Data;
import org.hage.platform.component.rate.measure.PerformanceRate;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;


@Data
@Immutable
class PerformanceRemoteMessage implements Serializable {
    private final PerformanceRate performanceRate;

    public static PerformanceRemoteMessage rateRequestMessage() {
        return new PerformanceRemoteMessage(null);
    }
}
