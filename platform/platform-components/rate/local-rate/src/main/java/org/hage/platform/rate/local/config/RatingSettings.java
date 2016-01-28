package org.hage.platform.rate.local.config;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.hage.platform.rate.model.MeasurerType;

import java.util.Map;

@Builder
public class RatingSettings {

    @Getter
    private final PerformanceRate minimalRate;
    @Getter
    private final GlobalRateSettings globalRateSettings;
    private final Map<MeasurerType, Boolean> enabledMeasurersMap;
    private final Map<MeasurerType, MeasurerSettings> measurerSettingsMap;

    public boolean measurerEnabled(PerformanceMeasurer measurer) {
        return enabledMeasurersMap.getOrDefault(measurer.getType(), false);
    }

    public NormalizationSettings normalizationFor(PerformanceMeasurer measurer) {
        return new NormalizationSettings(globalRateSettings, measurerSettingsMap.get(measurer.getType()));
    }

}
