package org.hage.platform.component.loadbalance.precondition;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.monitoring.ExecutionTimeStats;
import org.hage.platform.component.monitoring.SummaryAgentsStats;

@Data
public class DynamicNodeStats {
    private final NodeAddress nodeAddress;
    private final ExecutionTimeStats executionTimeStats;
    private final SummaryAgentsStats summaryAgentsStats;
}
