package org.hage.platform.component.loadbalance.input;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.balancing.BalancingInput;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.platform.component.loadbalance.knapsack.MappingContext;
import org.hage.util.proportion.Proportions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class BalanceInputCreator {


    @Autowired
    private KnapsackAllocationsCreator allocationsCreator;
    @Autowired
    private KnapsackProportionsCalculator proportionsCalculator;

    public BalancingInput createForKnapsacks(List<Knapsack> knapsacks, MappingContext mappingContext) {

        log.debug("Create balancing input for knapsacks {}", knapsacks);

        final long summarySize = calculateSummarySize(knapsacks);

        Proportions<KnapsackNormalizedCapacity> proportions = proportionsCalculator.calculateProportions(knapsacks, mappingContext);
        List<KnapsackAllocation> unbalancedKnapsacks = filterUnbalanced(allocationsCreator.createAllocations(summarySize, proportions));

        BalancingInput balancingInput = new BalancingInput(unbalancedKnapsacks);

        log.info("Balancing input {} created for knapsacks {}", balancingInput, knapsacks);

        return balancingInput;
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
