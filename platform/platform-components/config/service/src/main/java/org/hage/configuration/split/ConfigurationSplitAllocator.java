package org.hage.configuration.split;

import org.hage.platform.component.rate.cluster.AggregatedPerformanceMeasurements;
import org.hage.platform.config.ComputationConfiguration;

import java.util.List;

public interface ConfigurationSplitAllocator {
    List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements);
}
