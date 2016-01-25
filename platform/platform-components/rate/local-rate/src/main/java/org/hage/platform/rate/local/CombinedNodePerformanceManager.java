package org.hage.platform.rate.local;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.local.config.MeasurerSettings;
import org.hage.platform.rate.local.config.NormalizationRateSettings;
import org.hage.platform.rate.local.config.RateConfigurationService;
import org.hage.platform.rate.local.config.RatingSettings;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.hage.platform.rate.local.normalize.RateNormalizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.rate.local.util.PerformanceRateArithmetic.sumWithDefaultIfZero;

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

        RatingSettings ratingSettings = rateConfigurationService.getSettings();

        List<PerformanceRate> rates = getAllNormalizedRates(ratingSettings);
        PerformanceRate resultRate = sumWithDefaultIfZero(rates, ratingSettings.getMinimalRate());

        log.info("Overall performance is {}", resultRate);

        return resultRate;
    }

    private List<PerformanceRate> getAllNormalizedRates(RatingSettings ratingSettings) {
        return measurers
                .stream()
                .filter(ratingSettings::measurerEnabled)
                .map(measurer -> getNormalizedRate(measurer, ratingSettings))
                .collect(toList());
    }

    private PerformanceRate getNormalizedRate(PerformanceMeasurer measurer, RatingSettings settings) {
        log.debug("Getting normalized rate for measurer {}", measurer);

        NormalizationRateSettings measurerSettings = settings.getNormalizationSettingsFor(measurer);

        PerformanceRate rate = normalizationProvider.getNormalizer(measurerSettings).normalize(measurer.measureRate());

        return calculateWeightedRate(rate, measurerSettings.getMeasurerSettings());
    }

    private PerformanceRate calculateWeightedRate(PerformanceRate rate, MeasurerSettings settings) {
        log.debug("Calculating weighted rate for {} with settings {}", rate, settings);

        int categorySummaryWeight = settings.getBaseWeight() + settings.getWeight();
        return rate.multiply(categorySummaryWeight);
    }

    @Required
    public void setMeasurers(Collection<PerformanceMeasurer> measurers) {
        log.debug("Setting performance measurers: {}", measurers);

        this.measurers.clear();
        this.measurers.addAll(measurers);
    }
}
