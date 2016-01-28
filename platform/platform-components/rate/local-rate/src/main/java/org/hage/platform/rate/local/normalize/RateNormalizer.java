package org.hage.platform.rate.local.normalize;

import org.hage.platform.rate.local.measure.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
