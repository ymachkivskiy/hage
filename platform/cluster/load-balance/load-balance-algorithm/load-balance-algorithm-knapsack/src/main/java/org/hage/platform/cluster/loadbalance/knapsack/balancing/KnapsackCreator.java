package org.hage.platform.cluster.loadbalance.knapsack.balancing;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.hage.platform.node.execution.monitor.DynamicExecutionInfo;
import org.hage.platform.cluster.loadbalance.knapsack.model.Item;
import org.hage.platform.cluster.loadbalance.knapsack.model.Knapsack;
import org.hage.platform.cluster.loadbalance.knapsack.model.KnapsackContext;
import org.hage.platform.cluster.loadbalance.knapsack.model.MappingContext;
import org.hage.platform.cluster.loadbalance.knapsack.util.CalculationUtils;
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

    private List<Item> createItems(DynamicExecutionInfo dynamicExecutionInfo, MappingContext mapping) {
        return dynamicExecutionInfo.getUnitAgentsNumberInfos()
            .stream()
            .filter(CalculationUtils::validForComputation)
            .map(mapping::itemForStats)
            .collect(toList());
    }

}
