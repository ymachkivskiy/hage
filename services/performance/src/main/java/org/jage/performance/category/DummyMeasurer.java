package org.jage.performance.category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMeasurer implements PerformanceMeasurer {

    public static final DummyMeasurer INSTANCE = new DummyMeasurer();

    @Override
    public PerformanceRate measure() {
        PerformanceRate result = PerformanceRate.DUMMY;
        log.info("Default measure rate {}", result);
        return result;
    }
}
