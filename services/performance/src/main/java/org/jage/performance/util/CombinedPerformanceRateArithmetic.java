package org.jage.performance.util;

import org.jage.performance.rate.CombinedPerformanceRate;

import java.math.BigInteger;
import java.util.stream.Stream;

public abstract class CombinedPerformanceRateArithmetic {
    private CombinedPerformanceRateArithmetic() {
    }

    public CombinedPerformanceRate sum(CombinedPerformanceRate... rates) {
        BigInteger overallSum = Stream.of(rates)
                .map(CombinedPerformanceRate::getCombinedRate)
                .reduce(BigInteger.ZERO, BigInteger::add);
        return new CombinedPerformanceRate(overallSum);
    }

}
