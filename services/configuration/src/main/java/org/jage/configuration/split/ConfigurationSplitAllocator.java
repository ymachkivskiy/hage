package org.jage.configuration.split;

import org.jage.performance.cluster.AggregatedPerformanceMeasurements;
import org.jage.platform.config.ComputationConfiguration;

import java.util.List;

public interface ConfigurationSplitAllocator {
    List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements);
}
