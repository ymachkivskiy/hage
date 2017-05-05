package org.hage.platform.component.simulationconfig.division;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.rate.cluster.ActiveClusterPerformance;
import org.hage.platform.node.rate.cluster.NodeAbsolutePerformance;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.ConfigurationDivisor;
import org.hage.platform.component.simulationconfig.endpoint.AllocationPart;
import org.hage.util.proportion.Division;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hage.util.proportion.Proportions.forCountable;

@SingletonComponent
@Slf4j
public class ConfigurationAllocator {

    @Autowired
    private ConfigurationDivisor divisor;

    public Allocation byPerformance(Configuration configuration, ActiveClusterPerformance clusterPerformance) {

        Collection<NodeAbsolutePerformance> nodePerformances = clusterPerformance.getNodeAbsolutePerformances();

        Division<Configuration> configurationDivision = divisor.divideUsingProportions(configuration, forCountable(nodePerformances));

        return new Allocation(toAllocations(nodePerformances, configurationDivision));
    }

    private List<AllocationPart> toAllocations(Collection<NodeAbsolutePerformance> nodePerformances, Division<Configuration> configurationDivision) {
        return nodePerformances
            .stream()
            .map(co -> new ConfigPairWrapper(configurationDivision.getFor(co), co))
            .map(ConfigPairWrapper::toAllocationPart)
            .collect(toList());
    }

    @RequiredArgsConstructor
    private static class ConfigPairWrapper {

        private final Configuration configuration;
        private final NodeAbsolutePerformance nodeAbsolutePerformance;

        public AllocationPart toAllocationPart() {
            return new AllocationPart(configuration, nodeAbsolutePerformance.getNodeAddress());
        }

    }

}
