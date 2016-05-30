package org.hage.platform.component.loadbalance;

import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.hage.platform.component.loadbalance.knapsack.KnapsackCreator;
import org.hage.platform.component.loadbalance.partition.Partition;
import org.hage.platform.component.loadbalance.partition.PartitionCreator;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.ClusterBalanceCalculator;
import org.hage.platform.component.loadbalance.rebalance.NodeDynamicStats;
import org.hage.platform.component.loadbalance.reorganize.KnapsackTransfer;
import org.hage.platform.component.loadbalance.reorganize.PartitionBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.*;

@Component
public class KnapsackBalanceCalculator implements ClusterBalanceCalculator {

    @Autowired
    private KnapsackCreator knapsackCreator;
    @Autowired
    private PartitionCreator partitionCreator;
    @Autowired
    private PartitionBalancer partitionBalancer;

    @Override
    public List<BalanceOrder> calculateBalanceOrders(List<NodeDynamicStats> stats) {

        List<Knapsack> knapsacks = createKnapsacks(stats);
        Partition partition = partitionCreator.createPartition(knapsacks);
        List<KnapsackTransfer> knapsackTransfers = partitionBalancer.balancePartition(partition);

        return toOrders(knapsackTransfers);
    }

    private List<Knapsack> createKnapsacks(List<NodeDynamicStats> stats) {
        return stats.stream()
            .map(knapsackCreator::createFromStats)
            .collect(toList());
    }

    private List<BalanceOrder> toOrders(List<KnapsackTransfer> knapsackTransfers) {
        return knapsackTransfers.stream()
            .collect(
                collectingAndThen(
                    groupingBy(
                        KnapsackTransfer::getOriginNode,
                        mapping(
                            KnapsackTransfer::toRelocationOrder,
                            toList()
                        )
                    ),
                    m -> m.entrySet().stream().map(e -> new BalanceOrder(e.getKey(), e.getValue())).collect(toList())
                )
            );
    }

}
