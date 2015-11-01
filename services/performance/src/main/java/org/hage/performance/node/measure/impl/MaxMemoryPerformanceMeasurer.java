package org.hage.performance.node.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.performance.node.measure.PerformanceMeasurer;

@Slf4j
public class MaxMemoryPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public int measureRate() {
        long maxMemoInBytes = Runtime.getRuntime().maxMemory();
        long maxMemoInMBytes = maxMemoInBytes / (2 << 20);
        return (int) maxMemoInMBytes;
    }
}
