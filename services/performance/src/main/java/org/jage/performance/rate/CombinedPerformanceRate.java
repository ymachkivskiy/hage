package org.jage.performance.rate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.math.BigInteger;

@Immutable
@AllArgsConstructor
@ToString
public class CombinedPerformanceRate implements Serializable, Comparable<CombinedPerformanceRate> {

    @Getter
    private final BigInteger combinedRate;

    @Override
    public int compareTo(CombinedPerformanceRate o) {
        return combinedRate.compareTo(o.combinedRate);
    }
}
