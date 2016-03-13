package org.hage.platform.component.rate.config.data;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.measure.PerformanceRate;
import org.hage.platform.component.rate.model.MeasurerType;

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
