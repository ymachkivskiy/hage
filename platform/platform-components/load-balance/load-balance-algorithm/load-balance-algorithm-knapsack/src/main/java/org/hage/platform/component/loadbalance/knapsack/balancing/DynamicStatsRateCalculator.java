package org.hage.platform.component.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;
import org.hage.platform.component.execution.monitor.UnitSpecificAgentsStats;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

import static java.lang.Math.max;
import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.valueOf;
import static org.hage.platform.component.execution.phase.ExecutionPhaseType.MAIN__AGENTS_STEP;
import static org.hage.platform.component.execution.phase.ExecutionPhaseType.MAIN___CONTROL_AGENT_STEP;

@Slf4j
@Component
public class DynamicStatsRateCalculator {

    public BigDecimal calculateRate(DynamicExecutionInfo dynamicExecutionInfo) {
        log.debug("Rate dynamic stats: {}", dynamicExecutionInfo);

        long agentsNumber = getAgentsNumber(dynamicExecutionInfo);
        Duration executionTime = getExecutionTime(dynamicExecutionInfo);

        return rateUnitTime(calculateUnitTime(agentsNumber, executionTime));
    }

    private long getAgentsNumber(DynamicExecutionInfo dynamicExecutionInfo) {
        long agentsNumber = dynamicExecutionInfo.getUnitSpecificAgentsStats().stream()
            .mapToLong(UnitSpecificAgentsStats::getAgentsNumber)
            .sum();

        log.debug("Agents number is : {}", agentsNumber);

        return agentsNumber;
    }

    private Duration getExecutionTime(DynamicExecutionInfo dynamicExecutionInfo) {

        Duration summaryTime = dynamicExecutionInfo.getExecutionTimeStats().getDurationOf(
            MAIN___CONTROL_AGENT_STEP,
            MAIN__AGENTS_STEP
        );

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
