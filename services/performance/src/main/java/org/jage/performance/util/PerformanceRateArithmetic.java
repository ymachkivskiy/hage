package org.jage.performance.util;

import org.jage.performance.node.measure.PerformanceRate;

import java.util.stream.Stream;

public abstract class PerformanceRateArithmetic {
    private PerformanceRateArithmetic() {
    }

    public PerformanceRate sum(PerformanceRate... rates) {
        return Stream.of(rates).reduce(PerformanceRate.ZERO_RATE, PerformanceRate::add);
    }

}
