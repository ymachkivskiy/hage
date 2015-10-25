package org.jage.performance.node;

import org.jage.performance.rate.CombinedPerformanceRate;

public interface NodePerformanceManager {
    CombinedPerformanceRate getOverallPerformance();
}
