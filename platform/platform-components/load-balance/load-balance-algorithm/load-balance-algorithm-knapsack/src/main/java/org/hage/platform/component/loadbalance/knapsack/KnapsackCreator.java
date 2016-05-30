package org.hage.platform.component.loadbalance.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicStats;
import org.hage.platform.component.monitoring.DynamicStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class KnapsackCreator {

    @Autowired
    private DynamicStatsRateCalculator rateAnalyser;

    public Knapsack createFromStats(NodeDynamicStats nodeStats) {
        Knapsack knapsack = new Knapsack(nodeStats.getNodeAddress(), rateAnalyser.calculateRate(nodeStats.getDynamicStats()));

        createItems(nodeStats.getDynamicStats()).forEach(knapsack::addItem);

        log.info("Knapsack {} created for {}", knapsack, nodeStats);

        return knapsack;
    }

    private List<PositionItem> createItems(DynamicStats summaryAgentsStats) {
        return summaryAgentsStats.getUnitSpecificAgentsStats().stream()
            .map(stats -> new PositionItem(stats.getUnitPosition(), stats.getAgentsNumber()))
            .collect(toList());
    }

}
