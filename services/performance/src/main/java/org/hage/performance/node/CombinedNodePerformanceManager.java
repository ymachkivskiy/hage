package org.hage.performance.node;

import lombok.extern.slf4j.Slf4j;
import org.hage.performance.node.config.ConfigurationProperties;
import org.hage.performance.node.config.MeasurerRateConfiguration;
import org.hage.performance.node.config.NormalizationRateConfiguration;
import org.hage.performance.node.measure.PerformanceMeasurer;
import org.hage.performance.node.normalize.RateNormalizer;
import org.hage.performance.node.measure.PerformanceRate;
import org.hage.performance.node.normalize.RateNormalizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
class CombinedNodePerformanceManager implements NodePerformanceManager {

    private ConfigurationProperties configurationProperties = new ConfigurationProperties();
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
        MeasurerRateConfiguration rateConfig = configurationProperties.forMeasurer(measurer.getClass());

        return calculateWeightedRate(rate, rateConfig);
    }

    private PerformanceRate getNormalizedRate(PerformanceMeasurer measurer) {
        log.debug("Getting normalized rate for measurer {}", measurer);

        MeasurerRateConfiguration rateConfiguration = configurationProperties.forMeasurer(measurer.getClass());
        NormalizationRateConfiguration normalizationConfiguration = new NormalizationRateConfiguration(configurationProperties.getMaxGlobalRateConfig(), rateConfiguration);
        RateNormalizer normalizer = normalizationProvider.getNormalizer(normalizationConfiguration);

        return normalizer.normalize(measurer.measureRate());
    }


    private PerformanceRate calculateWeightedRate(PerformanceRate rate, MeasurerRateConfiguration configuration) {
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
