package org.hage.performance.node;

import org.hage.performance.node.measure.PerformanceRate;

public interface NodePerformanceManager {
    PerformanceRate getOverallPerformance();
}
