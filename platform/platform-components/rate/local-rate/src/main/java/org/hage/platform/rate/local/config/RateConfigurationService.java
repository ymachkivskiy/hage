package org.hage.platform.rate.local.config;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.measure.impl.ConcurrencyPerformanceMeasurer;
import org.hage.platform.rate.local.measure.impl.MaxMemoryPerformanceMeasurer;
import org.hage.platform.rate.local.normalize.GlobalRateSettings;
import org.hage.platform.rate.model.RateCalculationSettings;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class RateConfigurationService implements EventSubscriber {


    private final EventListener eventListener = new PrivateEventListener();

    private static final int CPU_CATEGORY_BASE_WEIGHT = 5;
    private static final int MEMORY_CATEGORY_BASE_WEIGHT = 4;

    private static final int CPU_CATEGORY_WEIGHT = 10; // simulation specific
    private static final int MEMORY_CATEGORY_WEIGHT = 3; // simulation specific
    //in number of available processors
    private static final int CONCURRENCY_CATEGORY_MAX = 64;
    //in mb
    private static final int MEMORY_CATEGORY_MAX = 64 * (2 << 10); //128 GB of RAM


    private static final int GLOBAL_MAX_CATEGORY_RATE = 10_000;

    private static final RateCalculationSettings DEFAULT_CONFIG = new RateCalculationSettings(1, 1, 1);




    private Map<Class<? extends PerformanceMeasurer>, RateCalculationSettings> categoryConfigurations = new HashMap<>();

    {
        categoryConfigurations.put(ConcurrencyPerformanceMeasurer.class, new RateCalculationSettings(CPU_CATEGORY_BASE_WEIGHT, CPU_CATEGORY_WEIGHT, CONCURRENCY_CATEGORY_MAX));
        categoryConfigurations.put(MaxMemoryPerformanceMeasurer.class, new RateCalculationSettings(MEMORY_CATEGORY_BASE_WEIGHT, MEMORY_CATEGORY_WEIGHT, MEMORY_CATEGORY_MAX));
    }


    public RateCalculationSettings getConfigurationForMeasurer(PerformanceMeasurer measurer) { //TODO
        RateCalculationSettings config = categoryConfigurations.get(measurer.getClass());

        if (config == null) {
            config = DEFAULT_CONFIG;
        }

        return config;
    }

    public GlobalRateSettings getGlobalRateSettings() {
        return new GlobalRateSettings(BigInteger.valueOf(GLOBAL_MAX_CATEGORY_RATE)); //TODO

    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @SuppressWarnings("unused")
        @Subscribe
        public void onComputationConfigurationUpdated(ConfigurationUpdatedEvent event) {
            boolean dupa = false;
        }
    }

}
