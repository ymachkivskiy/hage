package org.jage.performance.node.config;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.measure.PerformanceMeasurer;
import org.jage.performance.node.measure.impl.CpuPerformanceMeasurer;
import org.jage.performance.node.measure.impl.DiskPerformanceMeasurer;
import org.jage.performance.node.measure.impl.MemoryPerformanceMeasurer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConfigurationProperties {
    private static final int CPU_CATEGORY_BASE_WEIGHT = 5;
    private static final int MEMORY_CATEGORY_BASE_WEIGHT = 4;
    private static final int DISK_CATEGORY_BASE_WEIGHT = 1;

    private static final int CPU_CATEGORY_WEIGHT = 10;
    private static final int DISK_CATEGORY_WEIGHT = 4;
    private static final int MEMORY_CATEGORY_WEIGHT = 4;

    private static final int CPU_CATEGORY_MAX = 10_000;
    private static final int DISK_CATEGORY_MAX = 400;
    private static final int MEMORY_CATEGORY_MAX = 16_000_000;


    private static final int GLOBAL_MAX_CATEGORY_RATE = 1_000_000;

    private static final MeasurerRateConfiguration DEFAULT_CONFIG = new MeasurerRateConfiguration(0, 1, 1000);

    private Map<Class<? extends PerformanceMeasurer>, MeasurerRateConfiguration> categoryConfigurations = new HashMap<>();

    {
        categoryConfigurations.put(CpuPerformanceMeasurer.class, new MeasurerRateConfiguration(CPU_CATEGORY_BASE_WEIGHT, CPU_CATEGORY_WEIGHT, CPU_CATEGORY_MAX));
        categoryConfigurations.put(MemoryPerformanceMeasurer.class, new MeasurerRateConfiguration(MEMORY_CATEGORY_BASE_WEIGHT, MEMORY_CATEGORY_WEIGHT, MEMORY_CATEGORY_MAX));
        categoryConfigurations.put(DiskPerformanceMeasurer.class, new MeasurerRateConfiguration(DISK_CATEGORY_BASE_WEIGHT, DISK_CATEGORY_WEIGHT, DISK_CATEGORY_MAX));
    }

    public MeasurerRateConfiguration forMeasurer(Class<? extends PerformanceMeasurer> measurerType) {

        MeasurerRateConfiguration config = categoryConfigurations.get(measurerType);

        if (config == null) {
            log.warn("Querying configuration for unknown measurer {}. Using default configuration.", measurerType.getSimpleName());
            config = DEFAULT_CONFIG;
        }

        log.debug("Configuration for measurer {} is {}", measurerType.getSimpleName(), config);

        return config;
    }

    public GlobalRateConfiguration getMaxGlobalRateConfig() {
        return new GlobalRateConfiguration(BigInteger.valueOf(GLOBAL_MAX_CATEGORY_RATE));
    }


}
