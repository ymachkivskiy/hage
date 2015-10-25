package org.jage.performance.rate;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.PerformanceCategory;
import org.jage.performance.node.category.PerformanceRate;
import org.jage.performance.node.config.CategoryConfiguration;
import org.jage.performance.node.config.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class RateCombiner {

    private Map<PerformanceCategory, PerformanceRate> measuredRates = new EnumMap<>(PerformanceCategory.class);

    public RateCombiner addMeasurement(PerformanceCategory category, PerformanceRate rate) {
        log.info("Add measurement {} for category {}", rate, category);

        measuredRates.put(category, rate);
        return this;
    }

    public CombinedPerformanceRate calculateCombinedRate(ConfigurationProperties confProps) {
        log.info("Calculating combined rate");

        int overallRate = 0;

        for (Map.Entry<PerformanceCategory, PerformanceRate> entry : measuredRates.entrySet()) {
            PerformanceRate performanceRate = entry.getValue();
            PerformanceCategory category = entry.getKey();

            int categoryRate = calculateRate(performanceRate, confProps.forCategory(category));

            log.info("computed weighted rate {} for category {}", categoryRate, category);

            overallRate += categoryRate;
        }

        CombinedPerformanceRate resultRate = new CombinedPerformanceRate(overallRate);

        log.info("Node performance rate is {}", resultRate);

        return resultRate;
    }

    private int calculateRate(PerformanceRate rate, CategoryConfiguration conf) {
        return rate.getRate() * (conf.getCategoryBaseWeight() + conf.getCategoryWeight());
    }
}
