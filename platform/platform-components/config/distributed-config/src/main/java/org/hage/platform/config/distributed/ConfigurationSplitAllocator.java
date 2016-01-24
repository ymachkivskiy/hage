package org.hage.platform.config.distributed;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.rate.distributed.AggregatedPerformanceMeasurements;

import java.util.List;

public interface ConfigurationSplitAllocator {
    List<ConfigurationAllocation> allocateConfigurationParts(ComputationConfiguration configuration, AggregatedPerformanceMeasurements performanceMeasurements);
}
