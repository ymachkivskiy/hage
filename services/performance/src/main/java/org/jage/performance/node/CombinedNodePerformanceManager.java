package org.jage.performance.node;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.PerformanceRate;
import org.jage.performance.rate.RateCombiner;
import org.jage.performance.node.category.DummyMeasurer;
import org.jage.performance.node.category.PerformanceCategory;
import org.jage.performance.node.category.CategoryPerformanceMeasurer;
import org.jage.performance.node.config.ConfigurationProperties;
import org.jage.performance.rate.CombinedPerformanceRate;
import org.jage.performance.rate.normalize.RateNormalizationProvider;
import org.jage.performance.rate.normalize.RateNormalizer;
import org.jage.performance.rate.normalize.config.CategoryRateConfiguration;
import org.jage.performance.rate.normalize.config.RateConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
class CombinedNodePerformanceManager implements NodePerformanceManager {

    private ConfigurationProperties configurationProperties = new ConfigurationProperties();
    @Autowired
    private RateNormalizationProvider normalizationProvider;

    private Map<PerformanceCategory, CategoryPerformanceMeasurer> performanceMeasurers = new EnumMap<>(PerformanceCategory.class);

    {
        for (PerformanceCategory performanceCategory : PerformanceCategory.values()) {
            performanceMeasurers.put(performanceCategory, DummyMeasurer.INSTANCE);
        }
    }

    @Override
    public CombinedPerformanceRate getOverallPerformance() {
        log.info("Calculating overall node performance");

        RateCombiner rateCombiner = new RateCombiner();

        performanceMeasurers.forEach((cat, measurer) -> rateCombiner.addMeasurement(cat, getMeasurerRate(measurer)));

        CombinedPerformanceRate overallPerformance = rateCombiner.calculateCombinedRate(configurationProperties);

        log.info("Overall performance is {}", overallPerformance);

        return overallPerformance;
    }

    private PerformanceRate getMeasurerRate(CategoryPerformanceMeasurer measurer) {
        final CategoryRateConfiguration categoryRateConfiguration = measurer.getRateConfiguration();
        RateNormalizer normalizer = normalizationProvider.getNormalizer(new RateConfiguration(configurationProperties.getMaxGlobalRateConfig(), categoryRateConfiguration));

        return normalizer.normalize(measurer.measureRate());
    }


    public void setCpuPerformanceMeasurer(CategoryPerformanceMeasurer cpuCategoryPerformanceMeasurer) {
        log.info("Set CPU performance measurer {}", cpuCategoryPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.CPU, cpuCategoryPerformanceMeasurer);
    }

    public void setMemoryPerformanceMeasurer(CategoryPerformanceMeasurer memoryCategoryPerformanceMeasurer) {
        log.info("Set MEMORY performance measurer {}", memoryCategoryPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.MEMORY, memoryCategoryPerformanceMeasurer);
    }

    public void setDiskPerformanceMeasurer(CategoryPerformanceMeasurer diskCategoryPerformanceMeasurer) {
        log.info("Set DISK performance measurer {}", diskCategoryPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.DISK, diskCategoryPerformanceMeasurer);
    }

}
