package org.jage.configuration.service;

import com.google.common.eventbus.Subscribe;
import org.jage.address.node.NodeAddress;
import org.jage.bus.EventBus;
import org.jage.configuration.communication.ConfigurationRemoteChanel;
import org.jage.configuration.event.ConfigurationLoadedEvent;
import org.jage.performance.cluster.AggregatedPerformanceMeasurements;
import org.jage.performance.cluster.ClusterPerformanceManager;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Set;

public class ConfigurationSplitService implements IStatefulComponent {
    @Autowired
    private ConfigurationRemoteChanel configurationChanel;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private ClusterPerformanceManager clusterPerformanceManager;

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
        // trigger splitting with given configuration
        //1. ask who wants configuration
        Set<NodeAddress> nodes = configurationChanel.getNodesAvailableForComputations();
        //2. measure theirs performance
        AggregatedPerformanceMeasurements aggregatedPerformance = clusterPerformanceManager.getNodePerformances(nodes);
        //3. split configuration using 2.
        //4. send splited configuration from 3. for all nodes from 1.
        for (NodeAddress node : nodes) {
            configurationChanel.sendConfigurationToNode(event.getComputationConfiguration(), node);
        }
    }
}
