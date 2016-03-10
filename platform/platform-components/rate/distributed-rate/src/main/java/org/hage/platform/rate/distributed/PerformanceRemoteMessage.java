package org.hage.platform.rate.distributed;

import lombok.Data;
import org.hage.platform.rate.local.measure.PerformanceRate;

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
