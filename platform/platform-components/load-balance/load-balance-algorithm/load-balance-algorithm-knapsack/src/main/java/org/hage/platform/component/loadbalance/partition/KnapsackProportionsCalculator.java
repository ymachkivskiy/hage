package org.hage.platform.component.loadbalance.partition;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.util.proportion.Proportions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hage.util.proportion.Proportions.forCountable;

@Slf4j
@Component
class KnapsackProportionsCalculator {

    private static final BigDecimal MAX_INT_BD = BigDecimal.valueOf(Integer.MAX_VALUE);

    public Proportions<KnapsackNormalizedCapacity> calculateProportions(List<Knapsack> knapsacks) {

        log.debug("Calculate proportions for knapsacks {}", knapsacks);

        BigDecimal multiplier = calculateNormalizationMultiplier(knapsacks);

        List<KnapsackNormalizedCapacity> knapsackNormalizedCapacities = knapsacks
            .stream()
            .map(knapsack -> new KnapsackNormalizedCapacity(knapsack, multiplier))
            .collect(toList());

        log.info("Calculated normalized capacities for knapsacks {}", knapsackNormalizedCapacities);

        return forCountable(knapsackNormalizedCapacities);
    }

    private BigDecimal calculateNormalizationMultiplier(List<Knapsack> knapsacks) {

        BigDecimal maxRate = knapsacks.stream()
            .map(Knapsack::getRate)
            .max(BigDecimal::compareTo)
            .orElse(MAX_INT_BD);

        BigDecimal normalizationMultiplier = MAX_INT_BD.divide(maxRate, 32, BigDecimal.ROUND_DOWN);

        log.debug("Normalization multiplier is : {}", normalizationMultiplier);

        return normalizationMultiplier;
    }

}
