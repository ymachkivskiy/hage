package org.hage.platform.rate.local.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.model.MeasurerType;

@Slf4j
public class MaxMemoryPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public MeasurerType getType() {
        return MeasurerType.MAX_RAM_MEMORY;
    }

    @Override
    public int measureRate() {
        long maxMemoInBytes = Runtime.getRuntime().maxMemory();
        long maxMemoInMBytes = maxMemoInBytes / (2 << 20);
        return (int) maxMemoInMBytes;
    }
}
