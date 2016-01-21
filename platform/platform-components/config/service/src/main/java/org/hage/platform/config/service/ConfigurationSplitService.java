package org.hage.platform.config.service;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.address.node.NodeAddress;
import org.hage.platform.component.rate.cluster.AggregatedPerformanceMeasurements;
import org.hage.platform.component.rate.cluster.ClusterPerformanceManager;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.communication.ConfigurationRemoteChanel;
import org.hage.platform.config.event.ConfigurationLoadedEvent;
import org.hage.platform.config.split.ConfigurationAllocation;
import org.hage.platform.config.split.ConfigurationSplitAllocator;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Slf4j
public class ConfigurationSplitService implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Autowired
    private ConfigurationRemoteChanel configurationChanel;

    @Autowired
    private ClusterPerformanceManager clusterPerformanceManager;

    @Autowired
    private ConfigurationSplitAllocator configurationSplitter;


    private void splitAndSendConfiguration(ComputationConfiguration configuration) {
        log.info("Configuration has been loaded {}. Looking for nodes which require computation configuration", configuration);

        Set<NodeAddress> nodesWhichRequireConfiguration = configurationChanel.getNodesAvailableForComputations();

        log.info("Getting performance of found nodes");

        AggregatedPerformanceMeasurements aggregatedPerformance = clusterPerformanceManager.getNodePerformances(nodesWhichRequireConfiguration);

        log.info("Splitting configuration computation among found nodes");

        List<ConfigurationAllocation> allocatedConfiguration = configurationSplitter.allocateConfigurationParts(configuration, aggregatedPerformance);

        log.info("Sending computation configuration parts to found nodes");

        allocatedConfiguration.forEach(configurationChanel::sendConfiguration);
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @Subscribe
        public void onConfigurationLoaded(ConfigurationLoadedEvent event) {
            splitAndSendConfiguration(event.getComputationConfiguration());
        }

    }

}
