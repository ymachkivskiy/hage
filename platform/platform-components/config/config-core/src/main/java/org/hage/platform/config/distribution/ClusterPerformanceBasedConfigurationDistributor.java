package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.config.ConfigurationDistributor;
import org.hage.platform.component.rate.remote.ActiveClusterPerformance;
import org.hage.platform.component.rate.remote.ClusterPerformanceManager;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.distribution.division.Allocation;
import org.hage.platform.config.distribution.division.ConfigurationAllocator;
import org.hage.platform.config.distribution.endpoint.ConfigurationEndpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
class ClusterPerformanceBasedConfigurationDistributor implements ConfigurationDistributor {

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
