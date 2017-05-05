package org.hage.platform.cluster.loadbalance.knapsack.model;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;

import java.math.BigDecimal;

@Data
public class KnapsackContext {
    private final NodeAddress address;
    private final BigDecimal rate;
}
