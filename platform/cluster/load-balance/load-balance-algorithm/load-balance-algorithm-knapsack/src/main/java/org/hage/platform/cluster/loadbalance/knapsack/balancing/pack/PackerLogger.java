package org.hage.platform.cluster.loadbalance.knapsack.balancing.pack;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.cluster.loadbalance.knapsack.balancing.KnapsackAllocation;
import org.hage.platform.cluster.loadbalance.knapsack.util.AllocationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Aspect
@Order(0)
class PackerLogger {

    @Before(value = "org.hage.platform.cluster.loadbalance.knapsack.balancing.pack.PackerPointcuts.repackPointcut() && args(inputAllocations) && target(packer)", argNames = "inputAllocations,packer")
    private void logInput(Collection<KnapsackAllocation> inputAllocations, AllocationsPacker packer) {
        Logger log = LoggerFactory.getLogger(packer.getClass());

        if (log.isInfoEnabled()) {
            long inputSpread = AllocationUtils.calculateAllocationsSpread(inputAllocations);
            log.info("Spread rate is {} for input allocations {}", inputSpread, inputAllocations);
        }
    }

    @AfterReturning(pointcut = "org.hage.platform.cluster.loadbalance.knapsack.balancing.pack.PackerPointcuts.repackPointcut() && target(packer)", returning = "outputAllocations", argNames = "outputAllocations,packer")
    private void logOutput(Collection<KnapsackAllocation> outputAllocations, AllocationsPacker packer) {
        Logger log = LoggerFactory.getLogger(packer.getClass());
        if (log.isInfoEnabled()) {
            long outputSpread = AllocationUtils.calculateAllocationsSpread(outputAllocations);
            log.info("Spread rate is {} for repacked allocations {}", outputSpread, outputAllocations);
        }
    }

}
