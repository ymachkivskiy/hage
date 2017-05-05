package org.hage.platform.cluster.loadbalance.knapsack.balancing;

import lombok.Data;
import org.hage.platform.cluster.loadbalance.knapsack.model.Knapsack;

import static java.lang.Math.abs;

@Data
public class KnapsackAllocation {
    private final long capacity;
    private final Knapsack knapsack;

    public boolean isUnderCapacity() {
        return knapsack.getSize() < capacity;
    }

    public boolean isOverCapacity() {
        return knapsack.getSize() > capacity;
    }

    public boolean isBalanced() {
        return knapsack.getSize() == capacity;
    }

    public long getBalanceDiff() {
        return abs(knapsack.getSize() - capacity);
    }

    public KnapsackAllocation copy() {
        return new KnapsackAllocation(capacity, knapsack.copy());
    }

}
