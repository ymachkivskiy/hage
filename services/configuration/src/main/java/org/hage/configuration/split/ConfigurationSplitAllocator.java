package org.hage.configuration.split;

import org.hage.performance.cluster.AggregatedPerformanceMeasurements;
import org.hage.platform.config.ComputationConfiguration;

import java.util.List;

public interface ConfigurationSplitAllocator {
    List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements);
}
