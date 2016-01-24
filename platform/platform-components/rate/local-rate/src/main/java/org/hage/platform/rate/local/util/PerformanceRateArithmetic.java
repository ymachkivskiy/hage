package org.hage.platform.rate.local.util;

import org.hage.platform.rate.local.normalize.PerformanceRate;

import java.util.stream.Stream;

public abstract class PerformanceRateArithmetic {
    private PerformanceRateArithmetic() {
    }

    public PerformanceRate sum(PerformanceRate... rates) {
        return Stream.of(rates).reduce(PerformanceRate.ZERO_RATE, PerformanceRate::add);
    }

}
