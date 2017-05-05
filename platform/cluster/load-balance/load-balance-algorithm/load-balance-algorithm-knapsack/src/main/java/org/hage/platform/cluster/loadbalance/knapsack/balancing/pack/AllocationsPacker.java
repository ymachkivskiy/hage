package org.hage.platform.cluster.loadbalance.knapsack.balancing.pack;

import org.hage.platform.cluster.loadbalance.knapsack.balancing.KnapsackAllocation;

import java.util.Collection;

public interface AllocationsPacker {
    Collection<KnapsackAllocation> repack(Collection<KnapsackAllocation> knapsackAllocations);
}
