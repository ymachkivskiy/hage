package org.hage.platform.component.loadbalance.partition;

import com.google.common.primitives.UnsignedInteger;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.util.proportion.Countable;

import java.math.BigDecimal;
import java.math.BigInteger;

@ToString
class KnapsackNormalizedCapacity implements Countable {

    @Getter
    private final Knapsack knapsack;
    private final UnsignedInteger normalizedCapacity;

    KnapsackNormalizedCapacity(Knapsack knapsack, BigDecimal normalizationMultiplier) {
        this.knapsack = knapsack;
        this.normalizedCapacity = createNormalizedCapacity(knapsack, normalizationMultiplier);
    }

    private UnsignedInteger createNormalizedCapacity(Knapsack knapsack, BigDecimal normalizationMultiplier) {
        BigDecimal rate = knapsack.getRate();
        BigInteger normalizedRate = rate.multiply(normalizationMultiplier).toBigInteger();

        return UnsignedInteger.valueOf(normalizedRate);
    }

    public UnsignedInteger getNormalizedCapacity() {
        return normalizedCapacity;
    }

}
