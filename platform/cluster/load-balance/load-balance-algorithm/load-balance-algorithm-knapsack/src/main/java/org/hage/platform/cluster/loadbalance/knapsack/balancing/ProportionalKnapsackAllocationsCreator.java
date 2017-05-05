package org.hage.platform.cluster.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.loadbalance.knapsack.model.Knapsack;
import org.hage.util.proportion.Proportions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
class ProportionalKnapsackAllocationsCreator {

    public Set<KnapsackAllocation> createProportionalAllocations(long summarySize, Proportions<KnapsackNormalizedCapacity> proportions) {

        log.debug("Create allocations of size {} for knapsacks proportions {}", summarySize, proportions);

        Set<KnapsackAllocation> knapsackAllocations = createBaseAllocations(summarySize, proportions);
        adjustToSummarySize(summarySize, knapsackAllocations);

        log.info("Knapsack allocations created {}", knapsackAllocations);

        return knapsackAllocations;
    }

    private Set<KnapsackAllocation> createBaseAllocations(long summarySize, Proportions<KnapsackNormalizedCapacity> proportions) {
        return proportions.getFractions().stream()
            .map(cFraction -> createAllocation(summarySize, cFraction))
            .collect(toSet());
    }

    private KnapsackAllocation createAllocation(long summarySize, Proportions.CountableFraction<KnapsackNormalizedCapacity> cFraction) {
        BigDecimal fraction = cFraction.getFraction();
        long knapsackCapacity = fraction.multiply(BigDecimal.valueOf(summarySize)).longValue();
        return new KnapsackAllocation(knapsackCapacity, cFraction.getCountable().getKnapsack());
    }

    private void adjustToSummarySize(long summarySize, Set<KnapsackAllocation> knapsackAllocations) {

        long knapsacksSummaryCapacity = knapsackAllocations
            .stream()
            .mapToLong(KnapsackAllocation::getCapacity)
            .sum();
        long remainder = summarySize - knapsacksSummaryCapacity;

        if (remainder > 0) {

            Optional<KnapsackAllocation> hugestAllocation = knapsackAllocations
                .stream()
                .max((a1, a2) -> Long.compare(a1.getCapacity(), a2.getCapacity()));

            hugestAllocation.ifPresent(
                allocation -> {
                    long capacity = allocation.getCapacity();
                    Knapsack knapsack = allocation.getKnapsack();

                    knapsackAllocations.remove(allocation);
                    knapsackAllocations.add(new KnapsackAllocation(capacity + remainder, knapsack));
                }
            );

        }
    }

}
