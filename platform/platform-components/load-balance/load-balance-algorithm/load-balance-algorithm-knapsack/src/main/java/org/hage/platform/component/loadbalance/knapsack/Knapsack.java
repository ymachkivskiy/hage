package org.hage.platform.component.loadbalance.knapsack;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class Knapsack {

    private final NodeAddress address;
    private final BigDecimal rate;

    private long size = 0;

    private final Set<PositionItem> items = new HashSet<>();

    public void addItem(PositionItem positionItem) {
        if (items.add(positionItem)) {
            size += positionItem.getSize();
        }
    }

}
