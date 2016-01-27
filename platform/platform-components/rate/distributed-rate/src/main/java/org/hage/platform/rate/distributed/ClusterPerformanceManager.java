package org.hage.platform.rate.distributed;

import org.hage.platform.util.communication.address.node.NodeAddress;

import java.util.Set;

public interface ClusterPerformanceManager {
    AggregatedPerformanceMeasurements getClusterPerformance();

    AggregatedPerformanceMeasurements getNodePerformances(Set<NodeAddress> nodeAddresses);
}
