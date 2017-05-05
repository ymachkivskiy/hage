package org.hage.platform.component.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.knapsack.balancing.pack.AllocationsPacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class BalancingTransfersCalculator {

    @Autowired
    private AllocationsPacker knapsacksAllocationsPacker;
    @Autowired
    private KnapsackTransfersComputer knapsackTransfersComputer;

    public Collection<KnapsackTransfer> calculateTransfers(Collection<KnapsackAllocation> originalAllocations) {
        log.debug("Computing balancing transfers for {}", originalAllocations);

        Collection<KnapsackAllocation> repackedAllocations = knapsacksAllocationsPacker.repack(originalAllocations);

        log.debug("Repacked allocations are : {}", repackedAllocations);

        Collection<KnapsackTransfer> transfers = knapsackTransfersComputer.computeTransfers(originalAllocations, repackedAllocations);

        log.info("Computed balancing transfers for {} : {}", originalAllocations, transfers);

        return transfers;
    }

}
