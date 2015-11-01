package org.hage.configuration.split;

import org.hage.performance.cluster.AggregatedPerformanceMeasurements;
import org.hage.performance.cluster.NodeCombinedPerformance;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.ComputationConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This is currently dummy implementation of configuration splitting
 */
public class SimplePerformanceBasedConfigurationSplitAllocator implements ConfigurationSplitAllocator {
    @Override
    public List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements) {

        int componentsPerNode = Math.floorDiv(configuration.getLocalComponents().size(), performanceMeasurements.getNodesRateCombinedPerformances().size());
        if (componentsPerNode == 0) {
            componentsPerNode = 1;
        }

        Queue<IComponentDefinition> localComponentsQueue = new LinkedList<>(configuration.getLocalComponents());

        List<ConfigurationAllocation> result = new ArrayList<>(performanceMeasurements.getNodesRateCombinedPerformances().size());

        for (NodeCombinedPerformance nodeCombinedPerformance : performanceMeasurements.getNodesRateCombinedPerformances()) {
            List<IComponentDefinition> clusterLocalComponents = new LinkedList<>();

            int needToPeek = componentsPerNode;

            while (needToPeek-- > 0 && !localComponentsQueue.isEmpty()) {
                clusterLocalComponents.add(localComponentsQueue.remove());
            }

            ComputationConfiguration nodeComputation = ComputationConfiguration
                    .builder()
                    .globalComponents(configuration.getGlobalComponents())
                    .localComponents(clusterLocalComponents)
                    .build();

            result.add(new ConfigurationAllocation(nodeComputation, nodeCombinedPerformance.getAddress()));
        }

        if (!localComponentsQueue.isEmpty()) {
            ConfigurationAllocation firstAllocation = result.get(0);

            List<IComponentDefinition> appendedConfiguration = new ArrayList<>(localComponentsQueue);
            appendedConfiguration.addAll(firstAllocation.getConfiguration().getLocalComponents());

            ComputationConfiguration nodeComputation = ComputationConfiguration
                    .builder()
                    .globalComponents(firstAllocation.getConfiguration().getGlobalComponents())
                    .localComponents(appendedConfiguration)
                    .build();

            result.set(0, new ConfigurationAllocation(nodeComputation, firstAllocation.getDestinationNode()));
        }


        return result;
    }
}
