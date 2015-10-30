package org.jage.performance.node.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.measure.PerformanceMeasurer;

@Slf4j
public class CpuPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public int measureRate() {
        return 1;
    }
}
