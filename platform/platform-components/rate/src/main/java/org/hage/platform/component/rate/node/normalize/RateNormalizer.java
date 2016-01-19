package org.hage.platform.component.rate.node.normalize;

import org.hage.platform.component.rate.node.measure.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
