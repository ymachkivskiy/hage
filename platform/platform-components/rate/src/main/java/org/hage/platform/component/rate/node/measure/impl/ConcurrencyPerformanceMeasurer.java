package org.hage.platform.component.rate.node.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.node.measure.PerformanceMeasurer;

@Slf4j
public class ConcurrencyPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public int measureRate() {
        return Runtime.getRuntime().availableProcessors();
    }
}
