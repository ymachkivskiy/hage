package org.hage.platform.component.loadbalance.knapsack.balancing;

import lombok.Data;
import org.hage.platform.component.loadbalance.knapsack.model.Item;
import org.hage.platform.component.loadbalance.knapsack.model.Knapsack;


@Data
public class KnapsackTransfer {
    private final Knapsack origin;
    private final Item item;
    private final Knapsack destination;
}
