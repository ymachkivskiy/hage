package org.hage.platform.component.simulationconfig;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.rate.remote.ActiveClusterPerformance;
import org.hage.platform.component.rate.remote.ClusterPerformanceManager;
import org.hage.platform.component.simulationconfig.division.Allocation;
import org.hage.platform.component.simulationconfig.division.ConfigurationAllocator;
import org.hage.platform.component.simulationconfig.endpoint.ConfigurationEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
public class ClusterPerformanceBasedConfigurationDistributor implements ConfigurationDistributor {

    @Autowired
    private ConfigurationEndpoint endpoint;
    @Autowired
    private ClusterPerformanceManager performanceManager;
    @Autowired
    private ConfigurationAllocator allocator;


    @Override
    public void distribute(Configuration configuration) {

        log.debug("Configuration has been loaded {}. Looking for nodes which require computation configuration", configuration);

        Set<NodeAddress> addresses = endpoint.getNodesAvailableForComputations();

        log.debug("Nodes which require configuration '{}'", addresses);

        ActiveClusterPerformance performance = performanceManager.getNodePerformances(addresses);

        log.debug("Active cluster performance '{}'", performance);

        Allocation allocation = allocator.byPerformance(configuration, performance);

        endpoint.distribute(allocation);
    }

}
