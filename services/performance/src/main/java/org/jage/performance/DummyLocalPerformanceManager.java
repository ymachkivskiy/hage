package org.jage.performance;

public class DummyLocalPerformanceManager implements LocalPerformanceManager {

    private static final PerformanceRate DUMMY_RATE = new PerformanceRate(1);

    @Override
    public PerformanceRate getOverallPerformance() {
        return DUMMY_RATE;
    }

}
