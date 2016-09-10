package org.hage.platform.component.rate.config;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.rate.cluster.PerformanceRate;
import org.hage.platform.component.rate.config.data.GlobalRateSettings;
import org.hage.platform.component.rate.config.data.MeasurerSettings;
import org.hage.platform.component.rate.config.data.RatingSettings;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.rate.model.MeasurerType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static java.math.BigInteger.valueOf;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@SingletonComponent
public class RatingSettingsResolver {

    @Autowired
    private ExternalSettings externalSettings;


    public RatingSettings getSettingsUsing(ComputationRatingConfig simulationConfiguration) {
        return RatingSettings.builder()
            .globalRateSettings(new GlobalRateSettings(valueOf(externalSettings.getMaxCategoryRate())))
            .minimalRate(new PerformanceRate(valueOf(externalSettings.getMinimalRate())))
            .enabledMeasurersMap(createMeasurersEnabledMap(simulationConfiguration))
            .measurerSettingsMap(createMeasurersSettingsMap(simulationConfiguration))
            .build();
    }

    public RatingSettings getSettings() {
        return RatingSettings.builder()
            .globalRateSettings(new GlobalRateSettings(valueOf(externalSettings.getMaxCategoryRate())))
            .minimalRate(new PerformanceRate(valueOf(externalSettings.getMinimalRate())))
            .measurerSettingsMap(externalSettings.getMeasurerSettingsMap())
            .enabledMeasurersMap(externalSettings.getEnabledMeasurersMap())
            .build();
    }

    private Map<MeasurerType, Boolean> createMeasurersEnabledMap(ComputationRatingConfig config) {
        return stream(MeasurerType.values())
            .collect(toMap(
                identity(),
                mt -> config.getEnabledRateMeasureTypes().contains(mt))
            );
    }

    private Map<MeasurerType, MeasurerSettings> createMeasurersSettingsMap(ComputationRatingConfig simulationConfiguration) {
        Map<MeasurerType, MeasurerSettings> settingsMap = externalSettings.getMeasurerSettingsMap();

        for (MeasurerRateConfig rateConfig : simulationConfiguration.getMeasurerRateConfigs()) {

            MeasurerSettings baseSettings = settingsMap.get(rateConfig.getMeasurerType());
            MeasurerSettings updatedSettings = new MeasurerSettings(baseSettings.getBaseWeight(), rateConfig.getRateWeight(), rateConfig.getMaxRate());

            settingsMap.put(rateConfig.getMeasurerType(), updatedSettings);
        }

        return settingsMap;
    }

}
