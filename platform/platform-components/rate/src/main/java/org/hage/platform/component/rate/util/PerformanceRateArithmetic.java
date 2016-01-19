package org.hage.platform.component.rate.util;

import org.hage.platform.component.rate.node.measure.PerformanceRate;

import java.util.stream.Stream;

public abstract class PerformanceRateArithmetic {
    private PerformanceRateArithmetic() {
    }

    public PerformanceRate sum(PerformanceRate... rates) {
        return Stream.of(rates).reduce(PerformanceRate.ZERO_RATE, PerformanceRate::add);
    }

}
