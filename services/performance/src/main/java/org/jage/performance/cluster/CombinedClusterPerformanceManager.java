package org.jage.performance.cluster;


import lombok.extern.slf4j.Slf4j;
import org.jage.performance.cluster.communication.PerformanceRemoteChanel;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CombinedClusterPerformanceManager implements ClusterPerformanceManager {
    @Autowired
    private PerformanceRemoteChanel performanceRemoteChanel;


    @Override
    public ClusterPerformance getClusterPerformance() {

        log.info("Calculating cluster performance started");

        ClusterPerformance clusterPerformance = new ClusterPerformance(performanceRemoteChanel.getAllNodesPerformances());

        log.info("Calculating cluster performance finished. Calculated performance is {}", clusterPerformance);

        return clusterPerformance;
    }
}
