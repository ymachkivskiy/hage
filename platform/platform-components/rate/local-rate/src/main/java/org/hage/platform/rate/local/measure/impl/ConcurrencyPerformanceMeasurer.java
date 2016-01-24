package org.hage.platform.rate.local.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.model.MeasurerType;

@Slf4j
public class ConcurrencyPerformanceMeasurer implements PerformanceMeasurer {

    @Override
    public MeasurerType getType() {
        return MeasurerType.CONCURRENCY_PERFORMANCE;
    }

    @Override
    public int measureRate() {
        return Runtime.getRuntime().availableProcessors();
    }
}
