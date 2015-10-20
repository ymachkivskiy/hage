package org.jage.performance;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.category.DummyMeasurer;
import org.jage.performance.category.PerformanceCategory;
import org.jage.performance.category.PerformanceMeasurer;
import org.jage.performance.config.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
class CombinedNodePerformanceManager implements NodePerformanceManager {

    private ConfigurationProperties configurationProperties = new ConfigurationProperties();

    private Map<PerformanceCategory, PerformanceMeasurer> performanceMeasurers = new EnumMap<>(PerformanceCategory.class);

    {
        for (PerformanceCategory performanceCategory : PerformanceCategory.values()) {
            performanceMeasurers.put(performanceCategory, DummyMeasurer.INSTANCE);
        }
    }

    @Override
    public CombinedNodePerformanceRate getOverallPerformance() {
        log.info("Calculating overall node performance");

        RateCombiner rateCombiner = new RateCombiner();

        performanceMeasurers.forEach((cat, measurer) -> rateCombiner.addMeasurement(cat, measurer.measure()));

        CombinedNodePerformanceRate overallPerformance = rateCombiner.calculateCombinedRate(configurationProperties);

        log.info("Overall performance is {}", overallPerformance);

        return overallPerformance;
    }


    public void setCpuPerformanceMeasurer(PerformanceMeasurer cpuPerformanceMeasurer) {
        log.info("Set CPU performance measurer {}", cpuPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.CPU, cpuPerformanceMeasurer);
    }

    public void setMemoryPerformanceMeasurer(PerformanceMeasurer memoryPerformanceMeasurer) {
        log.info("Set MEMORY performance measurer {}", memoryPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.MEMORY, memoryPerformanceMeasurer);
    }

    public void setDiskPerformanceMeasurer(PerformanceMeasurer diskPerformanceMeasurer) {
        log.info("Set DISK performance measurer {}", diskPerformanceMeasurer);

        performanceMeasurers.put(PerformanceCategory.DISK, diskPerformanceMeasurer);
    }

}
