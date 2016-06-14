package org.hage.platform.component.loadbalance.knapsack.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.hage.platform.component.execution.monitor.UnitAgentsNumberInfo;
import org.hage.platform.component.structure.Position;

import static java.util.Optional.ofNullable;
import static org.hage.platform.component.loadbalance.knapsack.util.CalculationUtils.rateAgentsInfo;

public class MappingContext {

    private final BiMap<KnapsackContext, Knapsack> knapsackMapping = HashBiMap.create();
    private final BiMap<Position, Item> itemMapping = HashBiMap.create();


    public Knapsack newKnapsack(KnapsackContext knapsackContext) {
        return knapsackMapping.computeIfAbsent(knapsackContext, ctxt -> new Knapsack());
    }

    public KnapsackContext getContextForKnapsack(Knapsack knapsack) {
        return ofNullable(knapsackMapping.inverse().get(knapsack))
            .orElseThrow(() -> new IllegalArgumentException("Knapsack " + knapsack + " is not registered in mapping context"));
    }

    public Item itemForStats(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        return itemMapping.computeIfAbsent(unitAgentsNumberInfo.getUnitPosition(), pos -> new Item(rateAgentsInfo(unitAgentsNumberInfo)));
    }

    public Position getPositionOfItem(Item item) {
        return ofNullable(itemMapping.inverse().get(item))
            .orElseThrow(() -> new IllegalArgumentException("Item " + item + " is not registered in mapping context"));
    }

}
