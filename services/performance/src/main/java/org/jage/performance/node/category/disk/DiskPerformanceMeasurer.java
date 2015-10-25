package org.jage.performance.node.category.disk;


import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.PerformanceMeasurer;
import org.jage.performance.node.category.PerformanceRate;

@Slf4j
class DiskPerformanceMeasurer implements PerformanceMeasurer {
    @Override
    public PerformanceRate measure() {
        log.info("Request for performance rate");

        PerformanceRate rate = new PerformanceRate(1);
        //todo implement as: disk read-write speed

        log.info("Measured performance rate {}", rate);
        return rate;
    }
}
