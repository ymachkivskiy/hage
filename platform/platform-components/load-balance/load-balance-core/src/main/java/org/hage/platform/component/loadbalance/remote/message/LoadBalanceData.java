package org.hage.platform.component.loadbalance.remote.message;

import lombok.Data;
import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;

import java.io.Serializable;
import java.util.List;

@Data
public class LoadBalanceData implements Serializable {
    private final DynamicExecutionInfo dynamicExecutionInfo;
    private final List<UnitRelocationOrder> relocationOrders;
}
