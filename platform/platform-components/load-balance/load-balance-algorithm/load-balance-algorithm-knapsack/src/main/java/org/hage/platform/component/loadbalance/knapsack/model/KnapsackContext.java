package org.hage.platform.component.loadbalance.knapsack.model;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;

import java.math.BigDecimal;

@Data
public class KnapsackContext {
    private final NodeAddress address;
    private final BigDecimal rate;
}
