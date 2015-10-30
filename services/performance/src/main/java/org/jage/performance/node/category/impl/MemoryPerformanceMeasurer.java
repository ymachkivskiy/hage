package org.jage.performance.node.category.impl;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.PerformanceMeasurer;

@Slf4j
public class MemoryPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public int measureRate() {
        return 1;
    }
}
