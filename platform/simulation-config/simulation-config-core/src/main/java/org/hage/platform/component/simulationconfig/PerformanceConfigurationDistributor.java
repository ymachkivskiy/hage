package org.hage.platform.component.simulationconfig;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.rate.cluster.ActiveClusterPerformance;
import org.hage.platform.component.rate.cluster.ClusterPerformanceManager;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.simulationconfig.division.Allocation;
import org.hage.platform.component.simulationconfig.division.ConfigurationAllocator;
import org.hage.platform.component.simulationconfig.endpoint.ClusterSimulationConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
public class PerformanceConfigurationDistributor implements ConfigurationDistributor {

    @Autowired
    private ClusterSimulationConfigurator simulationConfigurator;
    @Autowired
    private ClusterPerformanceManager performanceManager;
    @Autowired
    private ConfigurationAllocator allocator;

    @Override
    public void distributeUsingRatingConfiguration(Configuration configuration, ComputationRatingConfig ratingConfig) {

        log.debug("Configuration has been loaded {}. Looking for nodes which require computation configuration", configuration);

        Set<NodeAddress> addresses = simulationConfigurator.getNodesAvailableForComputations();

        log.debug("Nodes which require configuration '{}'", addresses);

        ActiveClusterPerformance performance = performanceManager.getNodePerformances(addresses, ratingConfig);

        log.debug("Active cluster performance '{}'", performance);

        Allocation allocation = allocator.byPerformance(configuration, performance);

        simulationConfigurator.distribute(allocation);
    }

}
