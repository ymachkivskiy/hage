package org.hage.platform.rate.local;

import org.hage.platform.rate.local.normalize.PerformanceRate;

public interface NodePerformanceManager {
    PerformanceRate getOverallPerformance();
}
