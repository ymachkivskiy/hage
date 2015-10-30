package org.jage.performance.node.normalize;

import org.jage.performance.node.measure.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
