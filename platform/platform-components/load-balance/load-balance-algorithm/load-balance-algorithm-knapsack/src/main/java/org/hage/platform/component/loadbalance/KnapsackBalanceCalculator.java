package org.hage.platform.component.loadbalance;

import org.hage.platform.component.loadbalance.balancing.Balancer;
import org.hage.platform.component.loadbalance.balancing.BalancingInput;
import org.hage.platform.component.loadbalance.balancing.KnapsackTransfer;
import org.hage.platform.component.loadbalance.input.BalanceInputCreator;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.platform.component.loadbalance.knapsack.KnapsackCreator;
import org.hage.platform.component.loadbalance.knapsack.MappingContext;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicStats;
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
    private BalanceInputCreator balanceInputCreator;
    @Autowired
    private Balancer balancer;
    @Autowired
    private KnapsackTransfersToBalanceOrdersMapper transfersToOrdersMapper;

    @Override
    public List<BalanceOrder> calculateBalanceOrders(List<NodeDynamicStats> stats) {
        MappingContext mappingContext = new MappingContext();

        List<Knapsack> knapsacks = createKnapsacks(stats, mappingContext);
        BalancingInput balancingInput = balanceInputCreator.createForKnapsacks(knapsacks, mappingContext);
        Collection<KnapsackTransfer> knapsackTransfers = balancer.balance(balancingInput);

        return transfersToOrdersMapper.mapTransfersToOrders(knapsackTransfers, mappingContext);
    }

    private List<Knapsack> createKnapsacks(List<NodeDynamicStats> stats, MappingContext mappingContext) {
        return stats.stream()
            .map(nodeDynamicStats -> knapsackCreator.createFromStats(nodeDynamicStats, mappingContext))
            .collect(toList());
    }

}
