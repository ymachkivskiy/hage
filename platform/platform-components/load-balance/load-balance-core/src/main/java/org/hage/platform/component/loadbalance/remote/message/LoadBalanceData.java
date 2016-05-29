package org.hage.platform.component.loadbalance.remote.message;

import lombok.Data;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;
import org.hage.platform.component.monitoring.DynamicStats;

import java.io.Serializable;
import java.util.List;

@Data
public class LoadBalanceData implements Serializable {
    private final DynamicStats dynamicStats;
    private final List<UnitRelocationOrder> relocationOrders;
}
