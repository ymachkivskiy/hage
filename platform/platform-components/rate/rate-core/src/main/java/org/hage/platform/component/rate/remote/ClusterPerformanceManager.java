package org.hage.platform.component.rate.remote;



import org.hage.platform.component.cluster.NodeAddress;

import java.util.Set;

public interface ClusterPerformanceManager {
    ActiveClusterPerformance getNodePerformances(Set<NodeAddress> nodeAddresses);
}