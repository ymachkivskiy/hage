package org.jage.performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@AllArgsConstructor
@ToString
public class CombinedNodePerformanceRate implements Serializable, Comparable<CombinedNodePerformanceRate> {

    @Getter
    private final int rate;

    @Override
    public int compareTo(CombinedNodePerformanceRate o) {
        return rate - o.rate;
    }
}
