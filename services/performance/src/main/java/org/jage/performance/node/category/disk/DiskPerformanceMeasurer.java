package org.jage.performance.node.category.disk;


import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.CategoryPerformanceMeasurer;
import org.jage.performance.rate.normalize.config.CategoryRateConfiguration;

@Slf4j
class DiskPerformanceMeasurer implements CategoryPerformanceMeasurer {

    @Override
    public CategoryRateConfiguration getRateConfiguration() {
        return null;
    }

    @Override
    public int measureRate() {
        return 0;
    }
}
