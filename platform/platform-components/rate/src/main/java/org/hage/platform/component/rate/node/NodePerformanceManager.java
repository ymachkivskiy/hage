package org.hage.platform.component.rate.node;

import org.hage.platform.component.rate.node.measure.PerformanceRate;

public interface NodePerformanceManager {
    PerformanceRate getOverallPerformance();
}
