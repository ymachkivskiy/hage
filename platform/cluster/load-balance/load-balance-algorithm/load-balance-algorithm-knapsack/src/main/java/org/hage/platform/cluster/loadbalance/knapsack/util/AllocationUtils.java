package org.hage.platform.cluster.loadbalance.knapsack.util;

import org.hage.platform.cluster.loadbalance.knapsack.balancing.KnapsackAllocation;

import java.util.Collection;

public abstract class AllocationUtils {

    public static long calculateAllocationsSpread(Collection<KnapsackAllocation> allocations) {
        return allocations.stream()
            .mapToLong(KnapsackAllocation::getBalanceDiff)
            .sum();
    }


}
