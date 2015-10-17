package org.jage.performance;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PerformanceRate {
    private final int rate;

    public PerformanceRate(int rate) {
        this.rate = rate;
    }
}
