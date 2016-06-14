package org.hage.platform.component.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;
import org.hage.platform.component.loadbalance.knapsack.model.Item;
import org.hage.platform.component.loadbalance.knapsack.model.Knapsack;
import org.hage.platform.component.loadbalance.knapsack.model.KnapsackContext;
import org.hage.platform.component.loadbalance.knapsack.model.MappingContext;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class KnapsackCreator {

    @Autowired
    private DynamicStatsRateCalculator rateAnalyser;

    public Knapsack createFromStats(NodeDynamicExecutionInfo nodeStats, MappingContext mappingContext) {
        KnapsackContext knapsackContext = new KnapsackContext(nodeStats.getNodeAddress(), rateAnalyser.calculateRate(nodeStats.getDynamicExecutionInfo()));
        Knapsack knapsack = mappingContext.newKnapsack(knapsackContext);

        createItems(nodeStats.getDynamicExecutionInfo(), mappingContext).forEach(knapsack::addItem);

        log.info("Knapsack {} created for {}", knapsack, nodeStats);

        return knapsack;
    }

    private List<Item> createItems(DynamicExecutionInfo summaryAgentsStats, MappingContext mapping) {
        return summaryAgentsStats.getUnitSpecificAgentsStats().stream()
            .map(mapping::itemForStats)
            .collect(toList());
    }

}
