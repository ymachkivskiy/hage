package org.hage.performance.node.normalize;

import org.hage.performance.node.measure.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
