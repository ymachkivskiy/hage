package org.jage.performance.category.cpu;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.category.PerformanceMeasurer;
import org.jage.performance.category.PerformanceRate;

@Slf4j
class CpuPerformanceMeasurer implements PerformanceMeasurer {
    @Override
    public PerformanceRate measure() {
        log.info("Request for performance rate");

        PerformanceRate rate = new PerformanceRate(1);
        // todo implement as: available processors * processor speed

        log.info("Measured performance rate {}", rate);
        return rate;
    }
}
