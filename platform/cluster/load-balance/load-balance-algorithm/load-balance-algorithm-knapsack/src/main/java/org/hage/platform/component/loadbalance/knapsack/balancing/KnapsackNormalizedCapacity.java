package org.hage.platform.component.loadbalance.knapsack.balancing;

import com.google.common.primitives.UnsignedInteger;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.loadbalance.knapsack.model.Knapsack;
import org.hage.util.proportion.Countable;

import java.math.BigDecimal;

import static com.google.common.primitives.UnsignedInteger.valueOf;

@ToString
class KnapsackNormalizedCapacity implements Countable {

    @Getter
    private final Knapsack knapsack;
    private final UnsignedInteger normalizedCapacity;

    KnapsackNormalizedCapacity(Knapsack knapsack, BigDecimal normalizedRate) {
        this.knapsack = knapsack;
        this.normalizedCapacity = valueOf(normalizedRate.toBigInteger());
    }

    public UnsignedInteger getNormalizedCapacity() {
        return normalizedCapacity;
    }

}
