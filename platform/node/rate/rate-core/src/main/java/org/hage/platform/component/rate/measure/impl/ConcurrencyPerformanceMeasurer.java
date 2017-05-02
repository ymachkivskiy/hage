package org.hage.platform.component.rate.measure.impl;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.model.MeasurerType;

@Slf4j
public class ConcurrencyPerformanceMeasurer extends PerformanceMeasurer {

    public ConcurrencyPerformanceMeasurer() {
        super(MeasurerType.CONCURRENCY);
    }

    @Override
    public int measureRate() {
        return Runtime.getRuntime().availableProcessors();
    }
}
