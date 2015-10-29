package org.jage.performance.node.category;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.rate.normalize.config.CategoryRateConfiguration;

@Slf4j
public class DummyMeasurer implements CategoryPerformanceMeasurer {

    public static final DummyMeasurer INSTANCE = new DummyMeasurer();

    private static CategoryRateConfiguration CATEGORY_RATE_CONFIG = new CategoryRateConfiguration(100);

    @Override
    public CategoryRateConfiguration getRateConfiguration() {
        return CATEGORY_RATE_CONFIG;
    }

    @Override
    public int measureRate() {
        return 1;
    }
}
