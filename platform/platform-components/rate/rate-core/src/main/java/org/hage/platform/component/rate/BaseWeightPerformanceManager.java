package org.hage.platform.component.rate;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.rate.config.RatingSettingProvider;
import org.hage.platform.component.rate.config.data.MeasurerSettings;
import org.hage.platform.component.rate.config.data.NormalizationSettings;
import org.hage.platform.component.rate.config.data.RatingSettings;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.measure.PerformanceRate;
import org.hage.platform.component.rate.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.util.PerformanceRateArithmetic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
public class BaseWeightPerformanceManager implements PerformanceManager {


    @Autowired
    private RatingSettingProvider ratingSettingsProvider;
    @Autowired
    private RateNormalizationProvider normalizationProvider;
    @Autowired
    private Set<PerformanceMeasurer> measurers = new HashSet<>();

    @Override
    public PerformanceRate measurePerformance() {
        log.info("Calculating overall node performance");

        RatingSettings ratingSettings = ratingSettingsProvider.getSettings();

        List<PerformanceRate> rates = getAllNormalizedRates(ratingSettings);
        PerformanceRate resultRate = PerformanceRateArithmetic.sumWithDefaultIfZero(rates, ratingSettings.getMinimalRate());

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

        NormalizationSettings measurerSettings = settings.normalizationFor(measurer);

        PerformanceRate rate = normalizationProvider.getNormalizer(measurerSettings).normalize(measurer.measureRate());

        return calculateWeightedRate(rate, measurerSettings.getMeasurerSettings());
    }

    private PerformanceRate calculateWeightedRate(PerformanceRate rate, MeasurerSettings settings) {
        log.debug("Calculating weighted rate for {} with settings {}", rate, settings);

        int categorySummaryWeight = settings.getBaseWeight() + settings.getWeight();
        return rate.multiply(categorySummaryWeight);
    }

}
