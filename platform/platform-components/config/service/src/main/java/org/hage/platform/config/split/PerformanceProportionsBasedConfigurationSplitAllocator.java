package org.hage.platform.config.split;

import com.google.common.primitives.UnsignedInteger;
import lombok.Data;
import org.hage.address.node.NodeAddress;
import org.hage.platform.component.rate.cluster.AggregatedPerformanceMeasurements;
import org.hage.platform.component.rate.cluster.NodeCombinedPerformance;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.local.division.ConfigurationDivision;
import org.hage.platform.config.local.division.ConfigurationSplitter;
import org.hage.util.proportion.Countable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.google.common.primitives.UnsignedInteger.valueOf;
import static java.util.stream.Collectors.toList;
import static org.hage.util.proportion.Proportions.forCountable;

public class PerformanceProportionsBasedConfigurationSplitAllocator implements ConfigurationSplitAllocator {

    @Autowired
    private ConfigurationSplitter configurationSplitter;

    @Override
    public List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements) {

        List<RateCountableWithAddress> countable = performanceMeasurements.getNodesRateCombinedPerformances()
            .stream()
            .map(RateCountableWithAddress::new)
            .collect(toList());

        ConfigurationDivision configurationDivision = configurationSplitter.split(configuration, forCountable(countable));

        return countable
            .stream()
            .map(co -> new RateCountableConfiguration(configurationDivision.get(co), co))
            .filter(RateCountableConfiguration::hasConfiguration)
            .map(RateCountableConfiguration::toConfigurationAllocation)
            .collect(toList());
    }

    @Data
    private static class RateCountableWithAddress implements Countable {
        private final NodeCombinedPerformance nodeCombinedPerformance;

        @Override
        public UnsignedInteger getCount() {
            return valueOf(nodeCombinedPerformance.getPerformanceRate().getRate());
        }

        public NodeAddress getAddress() {
            return nodeCombinedPerformance.getAddress();
        }
    }

    @Data
    private static class RateCountableConfiguration {
        private final Optional<ComputationConfiguration> configuration;
        private final RateCountableWithAddress rateCountableWithAddress;

        public boolean hasConfiguration() {
            return configuration.isPresent();
        }

        public ConfigurationAllocation toConfigurationAllocation() {
            return new ConfigurationAllocation(configuration.get(), rateCountableWithAddress.getAddress());
        }
    }

}
