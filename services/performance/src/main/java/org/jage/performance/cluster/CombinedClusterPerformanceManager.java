package org.jage.performance.cluster;


import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.performance.cluster.communication.PerformanceRemoteChanel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
public class CombinedClusterPerformanceManager implements ClusterPerformanceManager {
    @Autowired
    private PerformanceRemoteChanel performanceRemoteChanel;

    @Override
    public AggregatedPerformanceMeasurements getClusterPerformance() {
        log.info("Calculating cluster performance started");

        AggregatedPerformanceMeasurements aggregatedPerformanceMeasurements = new AggregatedPerformanceMeasurements(performanceRemoteChanel.getAllNodesPerformances());

        log.info("Calculating cluster performance finished. Calculated performance is {}", aggregatedPerformanceMeasurements);

        return aggregatedPerformanceMeasurements;
    }

    @Override
    public AggregatedPerformanceMeasurements getNodePerformances(Set<NodeAddress> nodeAddresses) {
        log.info("Calculating nodes {} performance started", nodeAddresses);

        AggregatedPerformanceMeasurements aggregatedPerformanceMeasurements = new AggregatedPerformanceMeasurements(performanceRemoteChanel.getNodesPerformances(nodeAddresses));

        log.info("Calculating performance finished. Calculated performance is {}", aggregatedPerformanceMeasurements);

        return aggregatedPerformanceMeasurements;
    }
}
