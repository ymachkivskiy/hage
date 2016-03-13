package org.hage.platform.component.rate.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.model.MeasurerType;

@Slf4j
public class MaxMemoryPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public MeasurerType getType() {
        return MeasurerType.RAM_MEMORY;
    }

    @Override
    public int measureRate() {
        long maxMemoInBytes = Runtime.getRuntime().maxMemory();
        long maxMemoInMBytes = maxMemoInBytes / (2 << 20);
        return (int) maxMemoInMBytes;
    }
}
