package org.hage.platform.component.rate.cluster;

import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.rate.model.ComputationRatingConfig;

import java.util.Set;

public interface ClusterPerformanceManager {
    ActiveClusterPerformance getNodePerformances(Set<NodeAddress> nodeAddresses, ComputationRatingConfig ratingConfig);
}
