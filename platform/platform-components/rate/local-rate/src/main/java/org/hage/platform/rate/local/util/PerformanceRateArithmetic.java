package org.hage.platform.rate.local.util;

import org.hage.platform.rate.local.normalize.PerformanceRate;

import java.util.Collection;

import static java.util.Arrays.asList;

public abstract class PerformanceRateArithmetic {
    private PerformanceRateArithmetic() {
    }

    public static PerformanceRate sum(Collection<PerformanceRate> rates) {
        return rates.stream().reduce(PerformanceRate.ZERO_RATE, PerformanceRate::add);
    }

    public static PerformanceRate sum(PerformanceRate... rates) {
        return sum(asList(rates));
    }

    public static PerformanceRate sumWithDefaultIfZero(Collection<PerformanceRate> rates, PerformanceRate defaultRate) {
        PerformanceRate summingResult = sum(rates);
        if (summingResult.compareTo(PerformanceRate.ZERO_RATE) == 0) {
            return defaultRate;
        }
        return summingResult;
    }

}
