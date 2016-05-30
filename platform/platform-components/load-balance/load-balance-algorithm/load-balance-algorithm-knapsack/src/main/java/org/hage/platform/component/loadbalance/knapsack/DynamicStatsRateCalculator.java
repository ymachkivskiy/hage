package org.hage.platform.component.loadbalance.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.monitoring.DynamicStats;
import org.hage.platform.component.monitoring.UnitSpecificAgentsStats;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

import static java.lang.Math.max;
import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.valueOf;

@Slf4j
@Component
public class DynamicStatsRateCalculator {

    public BigDecimal calculateRate(DynamicStats dynamicStats) {
        log.debug("Rate dynamic stats: {}", dynamicStats);

        long agentsNumber = getAgentsNumber(dynamicStats);
        Duration executionTime = getExecutionTime(dynamicStats);

        return rateUnitTime(calculateUnitTime(agentsNumber, executionTime));
    }

    private long getAgentsNumber(DynamicStats dynamicStats) {
        long agentsNumber = dynamicStats.getUnitSpecificAgentsStats().stream()
            .mapToLong(UnitSpecificAgentsStats::getAgentsNumber)
            .sum();

        log.debug("Agents number is : {}", agentsNumber);

        return agentsNumber;
    }

    private Duration getExecutionTime(DynamicStats dynamicStats) {

        Duration agentsTime = dynamicStats.getExecutionTimeStats().getAgentsTime();
        Duration controlAgentsTime = dynamicStats.getExecutionTimeStats().getControlAgentsTime();
        Duration summaryTime = agentsTime.plus(controlAgentsTime);

        log.debug("Summary execution time is : {}", summaryTime);

        return summaryTime;
    }

    private Duration calculateUnitTime(long agentsNumber, Duration executionTime) {
        Duration unitTime = executionTime.dividedBy(max(1, agentsNumber));

        log.debug("Unit time is : {}", unitTime);

        return unitTime;
    }

    private BigDecimal rateUnitTime(Duration unitTime) {
        BigDecimal rate = valueOf(Long.MAX_VALUE);

        if (unitTime.compareTo(Duration.ZERO) != 0) {
            BigDecimal unitTimeSeconds = valueOf(unitTime.getSeconds()).add(valueOf(unitTime.getNano(), 9));
            rate = BigDecimal.ONE.divide(unitTimeSeconds, 9, ROUND_CEILING);
        }

        log.debug("Rate is : {}", rate);

        return rate;
    }

}
