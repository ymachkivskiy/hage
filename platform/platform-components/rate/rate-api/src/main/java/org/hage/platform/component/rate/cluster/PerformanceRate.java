package org.hage.platform.component.rate.cluster;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

@Immutable
@ToString
@EqualsAndHashCode(doNotUseGetters = true)
public class PerformanceRate implements Serializable, Comparable<PerformanceRate> {

    public static final PerformanceRate ZERO_RATE = new PerformanceRate(ZERO);

    @Getter
    private final BigInteger rate;

    public PerformanceRate(BigInteger rate) {
        checkArgument(rate.compareTo(ZERO) >= 0);
        this.rate = rate;
    }

    public PerformanceRate add(PerformanceRate other) {
        return new PerformanceRate(rate.add(other.rate));
    }

    public PerformanceRate multiply(int multiplier) {
        return new PerformanceRate(rate.multiply(valueOf(multiplier)));
    }

    @Override
    public int compareTo(PerformanceRate o) {
        return rate.compareTo(o.rate);
    }

    public static PerformanceRate sum(Collection<PerformanceRate> rates) {
        return rates.stream().reduce(ZERO_RATE, PerformanceRate::add);
    }

}
