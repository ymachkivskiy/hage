package org.jage.performance.rate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@AllArgsConstructor
@ToString
public class CombinedPerformanceRate implements Serializable, Comparable<CombinedPerformanceRate> {

    @Getter
    private final int rate;

    @Override
    public int compareTo(CombinedPerformanceRate o) {
        return rate - o.rate;
    }
}
