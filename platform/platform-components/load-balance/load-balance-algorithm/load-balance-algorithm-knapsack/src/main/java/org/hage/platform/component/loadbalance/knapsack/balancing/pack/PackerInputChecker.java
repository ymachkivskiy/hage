package org.hage.platform.component.loadbalance.knapsack.balancing.pack;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.loadbalance.knapsack.balancing.KnapsackAllocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@Aspect
@Order(1)
@Slf4j
class PackerInputChecker {

    @Before("PackerPointcuts.repackPointcut() && args(inputAllocations)")
    private void checkPackerInput(Collection<KnapsackAllocation> inputAllocations) {
        long capacitySummary = 0;
        long sizesSummary = 0;

        for (KnapsackAllocation allocation : inputAllocations) {
            capacitySummary += allocation.getCapacity();
            sizesSummary += allocation.getKnapsack().getSize();
        }

        if (capacitySummary != sizesSummary) {
            log.error("Invalid allocations packer input: summary capacity is {} summary knapsacks size is {}", capacitySummary, sizesSummary);
            throw new HageRuntimeException("Invalid allocations packer input: summary capacity differs from summary size");
        }

    }

}
