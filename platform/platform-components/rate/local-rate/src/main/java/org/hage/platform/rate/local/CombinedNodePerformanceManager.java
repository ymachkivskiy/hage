package org.hage.platform.rate.local;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.local.config.RateConfigurationService;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.normalize.NormalizationRateSettings;
import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.hage.platform.rate.local.normalize.RateNormalizationProvider;
import org.hage.platform.rate.local.normalize.RateNormalizer;
import org.hage.platform.rate.model.RateCalculationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
class CombinedNodePerformanceManager implements NodePerformanceManager {

    @Autowired
    private RateConfigurationService rateConfigurationService;
    @Autowired
    private RateNormalizationProvider normalizationProvider;

    private Set<PerformanceMeasurer> measurers = new HashSet<>();

    @Override
    public PerformanceRate getOverallPerformance() {
        log.info("Calculating overall node performance");

        PerformanceRate overallRate = measurers
                .stream()
                .map(this::getMeasuredRate)
                .reduce(PerformanceRate.ZERO_RATE, PerformanceRate::add);

        log.info("Overall performance is {}", overallRate);

        return overallRate;
    }

    private PerformanceRate getMeasuredRate(PerformanceMeasurer measurer) {
        log.debug("Getting measured rate from {}", measurer);

        PerformanceRate rate = getNormalizedRate(measurer);
        RateCalculationSettings rateConfig = rateConfigurationService.getConfigurationForMeasurer(measurer);

        return calculateWeightedRate(rate, rateConfig);
    }

    private PerformanceRate getNormalizedRate(PerformanceMeasurer measurer) {
        log.debug("Getting normalized rate for measurer {}", measurer);

        RateCalculationSettings rateConfiguration = rateConfigurationService.getConfigurationForMeasurer(measurer);
        NormalizationRateSettings normalizationConfiguration = new NormalizationRateSettings(rateConfigurationService.getGlobalRateSettings(), rateConfiguration);
        RateNormalizer normalizer = normalizationProvider.getNormalizer(normalizationConfiguration);

        return normalizer.normalize(measurer.measureRate());
    }


    private PerformanceRate calculateWeightedRate(PerformanceRate rate, RateCalculationSettings configuration) {
        log.debug("Calculating weighted rate for {} with configuration {}", rate, configuration);
        int categorySummaryWeight = configuration.getRateBaseWeight() + configuration.getRateWeight();
        return rate.multiply(categorySummaryWeight);
    }

    @Required
    public void setMeasurers(Collection<PerformanceMeasurer> measurers) {
        log.debug("Setting performance measurers: {}", measurers);

        this.measurers.clear();
        this.measurers.addAll(measurers);
    }
}
