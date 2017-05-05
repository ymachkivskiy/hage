package org.hage.platform.simconf.endpoint;

import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.simconf.division.Allocation;

import java.util.Set;

public interface ClusterSimulationConfigurator {
    void distribute(Allocation allocation);

    Set<NodeAddress> getNodesAvailableForComputations();
}
