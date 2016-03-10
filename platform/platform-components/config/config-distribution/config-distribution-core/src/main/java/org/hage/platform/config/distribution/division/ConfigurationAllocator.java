package org.hage.platform.config.distribution.division;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.distribution.endpoint.AllocationPart;
import org.hage.platform.rate.distributed.ActiveClusterPerformance;
import org.hage.platform.rate.distributed.NodeAbsolutePerformance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.hage.util.proportion.Proportions.forCountable;

@Slf4j
public class ConfigurationAllocator {

    @Autowired
    private ConfigurationDivisor divisor;

    public Allocation byPerformance(ComputationConfiguration configuration, ActiveClusterPerformance clusterPerformance) {

        Collection<NodeAbsolutePerformance> nodePerformances = clusterPerformance.getNodeAbsolutePerformances();

        ConfigurationDivision configurationDivision = divisor.divideUsingProportions(configuration, forCountable(nodePerformances));

        return new Allocation(toAllocations(nodePerformances, configurationDivision));
    }

    private List<AllocationPart> toAllocations(Collection<NodeAbsolutePerformance> nodePerformances, ConfigurationDivision configurationDivision) {
        return nodePerformances
            .stream()
            .map(co -> new ConfigPairWrapper(configurationDivision.getFor(co), co))
            .filter(ConfigPairWrapper::isValid)
            .map(ConfigPairWrapper::toAllocationPart)
            .collect(toList());
    }

    @RequiredArgsConstructor
    private static class ConfigPairWrapper {

        private final Optional<ComputationConfiguration> configuration;
        private final NodeAbsolutePerformance nodeAbsolutePerformance;

        public boolean isValid() {
            return configuration.isPresent();
        }

        public AllocationPart toAllocationPart() {
            return new AllocationPart(configuration.get(), nodeAbsolutePerformance.getNodeAddress());
        }

    }

}
