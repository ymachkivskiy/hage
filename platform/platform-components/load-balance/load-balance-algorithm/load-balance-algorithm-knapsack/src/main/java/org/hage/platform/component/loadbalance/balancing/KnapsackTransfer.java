package org.hage.platform.component.loadbalance.balancing;

import lombok.Data;
import org.hage.platform.component.loadbalance.knapsack.Item;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;


@Data
public class KnapsackTransfer {
    private final Knapsack origin;
    private final Item item;
    private final Knapsack destination;
}
