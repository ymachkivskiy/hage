package org.jage.performance;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.category.PerformanceCategory;
import org.jage.performance.category.PerformanceRate;
import org.jage.performance.config.CategoryConfiguration;
import org.jage.performance.config.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
class RateCombiner {

    private Map<PerformanceCategory, PerformanceRate> measuredRates = new EnumMap<>(PerformanceCategory.class);

    public RateCombiner addMeasurement(PerformanceCategory category, PerformanceRate rate) {
        log.info("Add measurement {} for category {}", rate, category);

        measuredRates.put(category, rate);
        return this;
    }

    public CombinedNodePerformanceRate calculateCombinedRate(ConfigurationProperties confProps) {
        log.info("Calculating combined rate");

        int overallRate = 0;

        for (Map.Entry<PerformanceCategory, PerformanceRate> entry : measuredRates.entrySet()) {
            PerformanceRate performanceRate = entry.getValue();
            PerformanceCategory category = entry.getKey();

            int categoryRate = calculateRate(performanceRate, confProps.forCategory(category));

            log.info("computed weighted rate {} for category {}", categoryRate, category);

            overallRate += categoryRate;
        }

        CombinedNodePerformanceRate resultRate = new CombinedNodePerformanceRate(overallRate);

        log.info("Node performance rate is {}", resultRate);

        return resultRate;
    }

    private int calculateRate(PerformanceRate rate, CategoryConfiguration conf) {
        return rate.getRate() * (conf.getCategoryBaseWeight() + conf.getCategoryWeight());
    }
}
