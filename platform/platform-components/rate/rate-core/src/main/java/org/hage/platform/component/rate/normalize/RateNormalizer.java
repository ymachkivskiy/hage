package org.hage.platform.component.rate.normalize;

import org.hage.platform.component.rate.measure.PerformanceRate;

public interface RateNormalizer {
    PerformanceRate normalize(int rate);
}
