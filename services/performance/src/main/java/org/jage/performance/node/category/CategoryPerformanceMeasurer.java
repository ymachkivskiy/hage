package org.jage.performance.node.category;

import org.jage.performance.rate.normalize.config.CategoryRateConfiguration;

public interface CategoryPerformanceMeasurer {
    CategoryRateConfiguration getRateConfiguration();

    int measureRate();
}
