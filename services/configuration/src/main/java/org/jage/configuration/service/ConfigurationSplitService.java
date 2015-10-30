package org.jage.configuration.service;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.bus.EventBus;
import org.jage.configuration.communication.ConfigurationRemoteChanel;
import org.jage.configuration.event.ConfigurationLoadedEvent;
import org.jage.configuration.split.ConfigurationSplitAllocator;
import org.jage.configuration.split.ConfigurationAllocation;
import org.jage.performance.cluster.AggregatedPerformanceMeasurements;
import org.jage.performance.cluster.ClusterPerformanceManager;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.config.ComputationConfiguration;
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
        eventBus.register(this);
    }

    @Override
    public boolean finish() throws ComponentException {
        eventBus.unregister(this);
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
