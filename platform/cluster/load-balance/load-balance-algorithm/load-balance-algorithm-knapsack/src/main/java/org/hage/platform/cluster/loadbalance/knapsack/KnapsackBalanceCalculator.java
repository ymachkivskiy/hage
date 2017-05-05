package org.hage.platform.cluster.loadbalance.knapsack;

import org.hage.platform.cluster.loadbalance.knapsack.balancing.*;
import org.hage.platform.cluster.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.cluster.loadbalance.rebalance.NodeDynamicExecutionInfo;
import org.hage.platform.cluster.loadbalance.knapsack.model.Knapsack;
import org.hage.platform.cluster.loadbalance.knapsack.model.MappingContext;
import org.hage.platform.cluster.loadbalance.rebalance.ClusterBalanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class KnapsackBalanceCalculator implements ClusterBalanceCalculator {

    @Autowired
    private KnapsackCreator knapsackCreator;
    @Autowired
    private KnapsackAllocationsCreator balanceInputCreator;
    @Autowired
    private BalancingTransfersCalculator balancingTransfersCalculator;
    @Autowired
    private KnapsackTransfersToBalanceOrdersMapper transfersToOrdersMapper;

    @Override
    public List<BalanceOrder> calculateBalanceOrders(List<NodeDynamicExecutionInfo> stats) {
        MappingContext mappingContext = new MappingContext();

        List<Knapsack> knapsacks = createKnapsacks(stats, mappingContext);
        Collection<KnapsackAllocation> knapsackAllocations = balanceInputCreator.createForKnapsacks(knapsacks, mappingContext);
        Collection<KnapsackTransfer> knapsackTransfers = balancingTransfersCalculator.calculateTransfers(knapsackAllocations);

        return transfersToOrdersMapper.mapTransfersToOrders(knapsackTransfers, mappingContext);
    }

    private List<Knapsack> createKnapsacks(List<NodeDynamicExecutionInfo> stats, MappingContext mappingContext) {
        return stats.stream()
            .map(nodeDynamicStats -> knapsackCreator.createFromStats(nodeDynamicStats, mappingContext))
            .collect(toList());
    }

}
