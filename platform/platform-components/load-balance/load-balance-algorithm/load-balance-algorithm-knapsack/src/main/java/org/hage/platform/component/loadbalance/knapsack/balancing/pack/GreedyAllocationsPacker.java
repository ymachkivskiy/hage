package org.hage.platform.component.loadbalance.knapsack.balancing.pack;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.knapsack.balancing.KnapsackAllocation;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Slf4j
public class GreedyAllocationsPacker implements AllocationsPacker {

    @Override
    public Collection<KnapsackAllocation> repack(Collection<KnapsackAllocation> knapsackAllocations) {


        return knapsackAllocations;

    }

    private Collection<KnapsackAllocation> getUnbalanced(Collection<KnapsackAllocation> knapsackAllocations) {
        return knapsackAllocations.stream()
            .filter(allocation -> !allocation.isBalanced())
            .collect(toList());
    }

}
