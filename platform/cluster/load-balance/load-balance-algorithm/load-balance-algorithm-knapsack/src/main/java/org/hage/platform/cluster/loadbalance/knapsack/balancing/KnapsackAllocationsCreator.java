package org.hage.platform.cluster.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.loadbalance.knapsack.model.Knapsack;
import org.hage.platform.cluster.loadbalance.knapsack.model.MappingContext;
import org.hage.util.proportion.Proportions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class KnapsackAllocationsCreator {

    @Autowired
    private ProportionalKnapsackAllocationsCreator allocationsCreator;
    @Autowired
    private KnapsackProportionsCalculator proportionsCalculator;

    public Collection<KnapsackAllocation> createForKnapsacks(List<Knapsack> knapsacks, MappingContext mappingContext) {

        log.debug("Create balancing input for knapsacks {}", knapsacks);

        final long summarySize = calculateSummarySize(knapsacks);

        Proportions<KnapsackNormalizedCapacity> proportions = proportionsCalculator.calculateProportions(knapsacks, mappingContext);
        Set<KnapsackAllocation> proportionalAllocations = allocationsCreator.createProportionalAllocations(summarySize, proportions);

        log.info("Balancing input {} created for knapsacks {}", proportionalAllocations, knapsacks);

        return proportionalAllocations;
    }

    private long calculateSummarySize(List<Knapsack> knapsacks) {
        long summarySize = knapsacks.stream().mapToLong(Knapsack::getSize).sum();

        log.debug("Summary knapsacks size is : {}", summarySize);

        return summarySize;
    }




}
