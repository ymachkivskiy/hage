package org.jage.performance.category.memory;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.category.PerformanceMeasurer;
import org.jage.performance.category.PerformanceRate;

@Slf4j
class MemoryPerformanceMeasurer implements PerformanceMeasurer {
    @Override
    public PerformanceRate measure() {
        log.info("Request for performance rate");

        PerformanceRate rate = new PerformanceRate(1);
        //todo implement as: available jvm memory * memory speed

        log.info("Measured performance rate {}", rate);
        return rate;
    }
}
