package org.hage.platform.node.rate.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.node.rate.measure.PerformanceMeasurer;
import org.hage.platform.node.rate.model.MeasurerType;

@Slf4j
public class MaxMemoryPerformanceMeasurer extends PerformanceMeasurer {

    public MaxMemoryPerformanceMeasurer() {
        super(MeasurerType.RAM_MEMORY);
    }

    @Override
    public int measureRate() {
        long maxMemoInBytes = Runtime.getRuntime().maxMemory();
        long maxMemoInMBytes = maxMemoInBytes / (1 << 20);
        return (int) maxMemoInMBytes;
    }
}
