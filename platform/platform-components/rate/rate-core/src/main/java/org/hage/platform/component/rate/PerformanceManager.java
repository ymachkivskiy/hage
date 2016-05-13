package org.hage.platform.component.rate;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.rate.cluster.PerformanceRate;
import org.hage.platform.component.rate.config.RatingSettingsResolver;
import org.hage.platform.component.rate.config.data.MeasurerSettings;
import org.hage.platform.component.rate.config.data.NormalizationSettings;
import org.hage.platform.component.rate.config.data.RatingSettings;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.measure.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.rate.cluster.PerformanceRate.sum;

@SingletonComponent
@Slf4j
public class PerformanceManager {


    @Autowired
    private RatingSettingsResolver ratingSettingsResolver;
    @Autowired
    private RateNormalizationProvider normalizationProvider;
    @Autowired
    private Set<PerformanceMeasurer> measurers = new HashSet<>();

    public PerformanceRate measurePerformanceUsing(ComputationRatingConfig ratingConfig) {
        log.info("Calculating overall node performance");

        RatingSettings ratingSettings = ratingSettingsResolver.getSettingsUsing(ratingConfig);

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

        NormalizationSettings measurerSettings = settings.normalizationFor(measurer);

        PerformanceRate rate = normalizationProvider.getNormalizer(measurerSettings).normalize(measurer.measureRate());

        return calculateWeightedRate(rate, measurerSettings.getMeasurerSettings());
    }

    private PerformanceRate calculateWeightedRate(PerformanceRate rate, MeasurerSettings settings) {
        log.debug("Calculating weighted rate for {} with settings {}", rate, settings);

        int categorySummaryWeight = settings.getBaseWeight() + settings.getWeight();
        return rate.multiply(categorySummaryWeight);
    }

    private PerformanceRate sumWithDefaultIfZero(Collection<PerformanceRate> rates, PerformanceRate defaultRate) {
        PerformanceRate summingResult = sum(rates);
        if (summingResult.compareTo(PerformanceRate.ZERO_RATE) == 0) {
            return defaultRate;
        }
        return summingResult;
    }

}
