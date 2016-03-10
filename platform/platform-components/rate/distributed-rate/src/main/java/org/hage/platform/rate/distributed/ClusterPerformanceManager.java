package org.hage.platform.rate.distributed;


import org.hage.platform.communication.address.NodeAddress;

import java.util.Set;

public interface ClusterPerformanceManager {
    ActiveClusterPerformance getNodePerformances(Set<NodeAddress> nodeAddresses);
}
