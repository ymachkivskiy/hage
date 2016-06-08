package org.hage.platform.component.loadbalance.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.loadbalance.knapsack.balancing.KnapsackTransfer;
import org.hage.platform.component.loadbalance.knapsack.model.MappingContext;
import org.hage.platform.component.loadbalance.rebalance.BalanceOrder;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;
import org.hage.platform.component.structure.Position;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class KnapsackTransfersToBalanceOrdersMapper {

    public List<BalanceOrder> mapTransfersToOrders(Collection<KnapsackTransfer> knapsackTransfers, MappingContext mappingContext) {

        List<BalanceOrder> balanceOrders = flattenOrdersGrouping(groupRelocationOrdersByNode(knapsackTransfers, mappingContext));

        log.debug("Map knapsack transfers {} to balance orders {}", knapsackTransfers, balanceOrders);

        return balanceOrders;
    }

    private Map<NodeAddress, List<UnitRelocationOrder>> groupRelocationOrdersByNode(Collection<KnapsackTransfer> knapsackTransfers, MappingContext mappingContext) {
        return knapsackTransfers.stream()
            .collect(
                groupingBy(
                    transfer -> getOriginOfTransfer(transfer, mappingContext),
                    mapping(
                        transfer -> toRelocationOrder(transfer, mappingContext),
                        toList()
                    )
                )
            );
    }

    private NodeAddress getOriginOfTransfer(KnapsackTransfer transfer, MappingContext mappingContext) {
        return mappingContext.getContextForKnapsack(transfer.getOrigin()).getAddress();
    }

    private UnitRelocationOrder toRelocationOrder(KnapsackTransfer transfer, MappingContext mappingContext) {
        Position position = mappingContext.getPositionOfItem(transfer.getItem());
        NodeAddress targetAddress = mappingContext.getContextForKnapsack(transfer.getDestination()).getAddress();
        return new UnitRelocationOrder(position, targetAddress);
    }

    private List<BalanceOrder> flattenOrdersGrouping(Map<NodeAddress, List<UnitRelocationOrder>> collect) {
        return collect.entrySet()
            .stream()
            .map(e -> new BalanceOrder(e.getKey(), e.getValue()))
            .collect(toList());
    }
}
