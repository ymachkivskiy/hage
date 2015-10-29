package org.jage.performance.rate.normalize;

import org.jage.performance.node.category.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
