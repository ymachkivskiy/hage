package org.hage.platform.component.rate.node.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.node.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.node.measure.impl.ConcurrencyPerformanceMeasurer;
import org.hage.platform.component.rate.node.measure.impl.MaxMemoryPerformanceMeasurer;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConfigurationProperties {
    private static final int CPU_CATEGORY_BASE_WEIGHT = 5;
    private static final int MEMORY_CATEGORY_BASE_WEIGHT = 4;

    private static final int CPU_CATEGORY_WEIGHT = 10; // simulation specific
    private static final int MEMORY_CATEGORY_WEIGHT = 3; // simulation specific
    //in number of available processors
    private static final int CONCURRENCY_CATEGORY_MAX = 64;
    //in mb
    private static final int MEMORY_CATEGORY_MAX = 64 * (2 << 10); //128 GB of RAM


    private static final int GLOBAL_MAX_CATEGORY_RATE = 10_000;

    private static final MeasurerRateConfiguration DEFAULT_CONFIG = new MeasurerRateConfiguration(1, 1, 1);

    private Map<Class<? extends PerformanceMeasurer>, MeasurerRateConfiguration> categoryConfigurations = new HashMap<>();

    {
        categoryConfigurations.put(ConcurrencyPerformanceMeasurer.class, new MeasurerRateConfiguration(CPU_CATEGORY_BASE_WEIGHT, CPU_CATEGORY_WEIGHT, CONCURRENCY_CATEGORY_MAX));
        categoryConfigurations.put(MaxMemoryPerformanceMeasurer.class, new MeasurerRateConfiguration(MEMORY_CATEGORY_BASE_WEIGHT, MEMORY_CATEGORY_WEIGHT, MEMORY_CATEGORY_MAX));
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
