package org.hage.platform.config.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.node.NodeAddress;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.distribution.division.ConfigurationAllocation;
import org.hage.platform.config.distribution.division.ConfigurationSplitAllocator;
import org.hage.platform.config.distribution.link.ConfigurationRemoteChanel;
import org.hage.platform.rate.distributed.AggregatedPerformanceMeasurements;
import org.hage.platform.rate.distributed.ClusterPerformanceManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Slf4j
class ConfigurationDistributorImpl implements ConfigurationDistributor {

    @Autowired
    private ConfigurationRemoteChanel configurationChanel;

    @Autowired
    private ClusterPerformanceManager clusterPerformanceManager;

    @Autowired
    private ConfigurationSplitAllocator configurationSplitter;


    @Override
    public void distribute(ComputationConfiguration configuration) {
        log.info("Configuration has been loaded {}. Looking for nodes which require computation configuration", configuration);

        Set<NodeAddress> nodesWhichRequireConfiguration = configurationChanel.getNodesAvailableForComputations();

        log.info("Getting performance of found nodes");

        AggregatedPerformanceMeasurements aggregatedPerformance = clusterPerformanceManager.getNodePerformances(nodesWhichRequireConfiguration);

        log.info("Splitting configuration computation among found nodes");

        List<ConfigurationAllocation> allocatedConfiguration = configurationSplitter.allocateConfigurationParts(configuration, aggregatedPerformance);

        log.info("Sending computation configuration parts to found nodes");

        allocatedConfiguration.forEach(configurationChanel::sendConfiguration);
    }

}
