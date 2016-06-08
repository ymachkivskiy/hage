package org.hage.platform.component.loadbalance.knapsack.balancing.pack;

import org.hage.platform.component.loadbalance.knapsack.balancing.KnapsackAllocation;

import java.util.Collection;

public interface AllocationsPacker {
    Collection<KnapsackAllocation> repack(Collection<KnapsackAllocation> knapsackAllocations);
}
