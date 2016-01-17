package org.hage.configuration.service;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.address.node.NodeAddress;
import org.hage.configuration.communication.ConfigurationRemoteChanel;
import org.hage.configuration.event.ConfigurationLoadedEvent;
import org.hage.configuration.split.ConfigurationAllocation;
import org.hage.configuration.split.ConfigurationSplitAllocator;
import org.hage.performance.cluster.AggregatedPerformanceMeasurements;
import org.hage.performance.cluster.ClusterPerformanceManager;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

@Slf4j
public class ConfigurationSplitService implements IStatefulComponent {
    @Autowired
    private ConfigurationRemoteChanel configurationChanel;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private ClusterPerformanceManager clusterPerformanceManager;
    @Autowired
    private ConfigurationSplitAllocator configurationSplitter;

    @Override
    public void init() throws ComponentException {
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    @Subscribe
    public void onConfigurationLoaded(@Nonnull final ConfigurationLoadedEvent event) {
        final ComputationConfiguration configuration = event.getComputationConfiguration();

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
