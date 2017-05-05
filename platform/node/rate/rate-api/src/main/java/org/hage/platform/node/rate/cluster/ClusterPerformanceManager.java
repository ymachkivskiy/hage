package org.hage.platform.node.rate.cluster;

import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.rate.model.ComputationRatingConfig;

import java.util.Set;

public interface ClusterPerformanceManager {
    ActiveClusterPerformance getNodePerformances(Set<NodeAddress> nodeAddresses, ComputationRatingConfig ratingConfig);
}
