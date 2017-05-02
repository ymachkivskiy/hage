package org.hage.platform.component.rate.measure.normalize;

import org.hage.platform.component.rate.cluster.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
