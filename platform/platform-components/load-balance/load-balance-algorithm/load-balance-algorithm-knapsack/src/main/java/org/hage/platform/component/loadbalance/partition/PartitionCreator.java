package org.hage.platform.component.loadbalance.partition;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.util.proportion.Proportions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class PartitionCreator {


    @Autowired
    private KnapsackAllocationsCreator allocationsCreator;
    @Autowired
    private KnapsackProportionsCalculator proportionsCalculator;

    public Partition createPartition(List<Knapsack> knapsacks) {

        log.debug("Create partition for knapsacks {}", knapsacks);

        final long summarySize = calculateSummarySize(knapsacks);

        Proportions<KnapsackNormalizedCapacity> proportions = proportionsCalculator.calculateProportions(knapsacks);
        List<KnapsackAllocation> unbalancedKnapsacks = filterUnbalanced(allocationsCreator.createAllocations(summarySize, proportions));

        Partition partition = new Partition(unbalancedKnapsacks);

        log.info("Partition {} created for knapsacks {}", partition, knapsacks);

        return partition;
    }

    private long calculateSummarySize(List<Knapsack> knapsacks) {
        long summarySize = knapsacks.stream().mapToLong(Knapsack::getSize).sum();

        log.debug("Summary knapsacks size is : {}", summarySize);

        return summarySize;
    }


    private List<KnapsackAllocation> filterUnbalanced(Set<KnapsackAllocation> knapsackAllocations) {
        return knapsackAllocations.stream()
            .filter(allocation -> !allocation.isBalanced())
            .collect(toList());
    }

}
