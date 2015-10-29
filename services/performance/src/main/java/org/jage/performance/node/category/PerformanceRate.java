package org.jage.performance.node.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@AllArgsConstructor
@ToString
public class PerformanceRate {

    @Getter
    private final BigInteger rate;

}
