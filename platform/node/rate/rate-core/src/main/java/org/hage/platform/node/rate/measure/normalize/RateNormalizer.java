package org.hage.platform.node.rate.measure.normalize;

import org.hage.platform.node.rate.cluster.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
