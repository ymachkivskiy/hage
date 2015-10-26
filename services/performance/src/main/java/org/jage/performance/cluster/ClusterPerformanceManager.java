package org.jage.performance.cluster;

import org.jage.address.node.NodeAddress;

import java.util.Set;

public interface ClusterPerformanceManager {
    AggregatedPerformanceMeasurements getClusterPerformance();

    AggregatedPerformanceMeasurements getNodePerformances(Set<NodeAddress> nodeAddresses);
}
