package org.hage.performance.cluster;

import org.hage.address.node.NodeAddress;

import java.util.Set;

public interface ClusterPerformanceManager {
    AggregatedPerformanceMeasurements getClusterPerformance();

    AggregatedPerformanceMeasurements getNodePerformances(Set<NodeAddress> nodeAddresses);
}
