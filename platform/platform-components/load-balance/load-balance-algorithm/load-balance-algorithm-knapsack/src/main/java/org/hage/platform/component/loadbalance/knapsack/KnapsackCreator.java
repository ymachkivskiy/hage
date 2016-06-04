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

    public Knapsack createFromStats(NodeDynamicStats nodeStats, MappingContext mappingContext) {
        KnapsackContext knapsackContext = new KnapsackContext(nodeStats.getNodeAddress(), rateAnalyser.calculateRate(nodeStats.getDynamicStats()));
        Knapsack knapsack = mappingContext.newKnapsack(knapsackContext);

        createItems(nodeStats.getDynamicStats(), mappingContext).forEach(knapsack::addItem);

        log.info("Knapsack {} created for {}", knapsack, nodeStats);

        return knapsack;
    }

    private List<Item> createItems(DynamicStats summaryAgentsStats, MappingContext mapping) {
        return summaryAgentsStats.getUnitSpecificAgentsStats().stream()
            .map(mapping::itemForStats)
            .collect(toList());
    }

}
