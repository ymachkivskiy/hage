package org.jage.performance.node;

import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.category.PerformanceMeasurer;
import org.jage.performance.node.category.PerformanceRate;
import org.jage.performance.node.config.ConfigurationProperties;
import org.jage.performance.rate.CombinedPerformanceRate;
import org.jage.performance.rate.normalize.RateNormalizationProvider;
import org.jage.performance.rate.normalize.RateNormalizer;
import org.jage.performance.rate.normalize.config.NormalizationRateConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigInteger.valueOf;

@Slf4j
class CombinedNodePerformanceManager implements NodePerformanceManager {

    private ConfigurationProperties configurationProperties = new ConfigurationProperties();
    @Autowired
    private RateNormalizationProvider normalizationProvider;

    private Set<PerformanceMeasurer> measurers = new HashSet<>();

    @Override
    public CombinedPerformanceRate getOverallPerformance() {
        log.info("Calculating overall node performance");

        BigInteger overallRate = BigInteger.ZERO;

        for (PerformanceMeasurer measurer : measurers) {

            PerformanceRate rate = getNormalizedRate(measurer);
            MeasurerRateConfiguration rateConfig = configurationProperties.forMeasurer(measurer.getClass());

            overallRate = overallRate.add(calculateRate(rate, rateConfig));
        }

        CombinedPerformanceRate overallPerformance = new CombinedPerformanceRate(overallRate);

        log.info("Overall performance is {}", overallPerformance);

        return overallPerformance;
    }

    private PerformanceRate getNormalizedRate(PerformanceMeasurer measurer) {
        log.debug("Getting normalized rate for measurer {}", measurer);

        MeasurerRateConfiguration rateConfiguration = configurationProperties.forMeasurer(measurer.getClass());
        NormalizationRateConfiguration normalizationConfiguration = new NormalizationRateConfiguration(configurationProperties.getMaxGlobalRateConfig(), rateConfiguration);
        RateNormalizer normalizer = normalizationProvider.getNormalizer(normalizationConfiguration);

        return normalizer.normalize(measurer.measureRate());
    }


    private BigInteger calculateRate(PerformanceRate rate, MeasurerRateConfiguration configuration) {
        log.info("Calculating weighted rate for {} with configuration {}", rate, configuration);

        int categorySummaryWeight = configuration.getRateBaseWeight() + configuration.getRateWeight();
        return rate.getRate().multiply(valueOf(categorySummaryWeight));
    }

    @Required
    public void setMeasurers(Collection<PerformanceMeasurer> measurers) {
        log.info("Setting performance measurers: {}", measurers);

        this.measurers.clear();
        this.measurers.addAll(measurers);
    }
}
