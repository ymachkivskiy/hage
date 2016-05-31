package org.hage.platform.component.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;
import org.hage.platform.component.loadbalance.reorganize.KnapsackTransfer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
class KnapsackTransfersToBalanceOrdersMapper {

    public List<BalanceOrder> mapTransfersToOrders(List<KnapsackTransfer> knapsackTransfers) {

        List<BalanceOrder> balanceOrders = flattenOrdersGrouping(groupRelocationOrdersByNode(knapsackTransfers));

        log.debug("Map knapsack transfers {} to balance orders {}", knapsackTransfers, balanceOrders);

        return balanceOrders;
    }

    private Map<NodeAddress, List<UnitRelocationOrder>> groupRelocationOrdersByNode(List<KnapsackTransfer> knapsackTransfers) {
        return knapsackTransfers.stream()
            .collect(
                groupingBy(
                    KnapsackTransfer::getOriginNode,
                    mapping(
                        KnapsackTransfer::toRelocationOrder,
                        toList()
                    )
                )
            );
    }

    private List<BalanceOrder> flattenOrdersGrouping(Map<NodeAddress, List<UnitRelocationOrder>> collect) {
        return collect.entrySet()
            .stream()
            .map(e -> new BalanceOrder(e.getKey(), e.getValue()))
            .collect(toList());
    }
}
