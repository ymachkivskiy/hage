package org.jage.performance.util;

import org.jage.performance.rate.CombinedPerformanceRate;

import java.util.stream.Stream;

public abstract class CombinedPerformanceRateArithmetic {
    private CombinedPerformanceRateArithmetic() {
    }

    public CombinedPerformanceRate sum(CombinedPerformanceRate... rates) {
        int overallSum = Stream.of(rates)
                .mapToInt(CombinedPerformanceRate::getRate)
                .sum();
        return new CombinedPerformanceRate(overallSum);
    }

}
