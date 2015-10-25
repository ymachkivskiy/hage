package org.jage.performance.node.category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMeasurer implements PerformanceMeasurer {

    public static final DummyMeasurer INSTANCE = new DummyMeasurer();

    @Override
    public PerformanceRate measure() {
        PerformanceRate result = new PerformanceRate(1);
        log.info("Default measure rate {}", result);
        return result;
    }
}
