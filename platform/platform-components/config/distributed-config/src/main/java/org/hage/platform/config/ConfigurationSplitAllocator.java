package org.hage.platform.config;

import org.hage.platform.rate.distributed.AggregatedPerformanceMeasurements;

import java.util.List;

public interface ConfigurationSplitAllocator {
    List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements);
}
