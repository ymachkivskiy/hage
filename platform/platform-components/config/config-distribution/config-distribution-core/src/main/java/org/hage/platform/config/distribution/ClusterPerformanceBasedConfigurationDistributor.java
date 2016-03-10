package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.distribution.division.Allocation;
import org.hage.platform.config.distribution.division.ConfigurationAllocator;
import org.hage.platform.config.distribution.endpoint.ConfigurationEndpoint;
import org.hage.platform.rate.distributed.ActiveClusterPerformance;
import org.hage.platform.rate.distributed.ClusterPerformanceManager;
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
    public void distribute(ComputationConfiguration configuration) {

        log.debug("Configuration has been loaded {}. Looking for nodes which require computation configuration", configuration);

        Set<NodeAddress> addresses = endpoint.getNodesAvailableForComputations();

        log.debug("Nodes which require configuration '{}'", addresses);

        ActiveClusterPerformance performance = performanceManager.getNodePerformances(addresses);

        log.debug("Active cluster performance '{}'", performance);

        Allocation allocation = allocator.byPerformance(configuration, performance);

        endpoint.distribute(allocation);
    }

}