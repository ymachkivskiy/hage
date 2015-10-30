package org.jage.performance.node;

import org.jage.performance.node.measure.PerformanceRate;

public interface NodePerformanceManager {
    PerformanceRate getOverallPerformance();
}
