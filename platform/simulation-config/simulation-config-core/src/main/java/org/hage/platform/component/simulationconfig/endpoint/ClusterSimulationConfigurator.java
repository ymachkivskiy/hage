package org.hage.platform.component.simulationconfig.endpoint;

import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.simulationconfig.division.Allocation;

import java.util.Set;

public interface ClusterSimulationConfigurator {
    void distribute(Allocation allocation);

    Set<NodeAddress> getNodesAvailableForComputations();
}
