package org.hage.platform.component.rate.config;

import lombok.Getter;
import org.hage.platform.component.rate.config.data.MeasurerSettings;
import org.hage.platform.component.rate.model.MeasurerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Component
public class ExternalSettings {

    private static final String PROPERTY_PREFIX = "hage.platform.rate.local.";

    private static final String G_MINIMAL_RATE_SUFFIX = "minimalRate";
    private static final String G_MAX_CATEGORY_RATE_SUFFIX = "maxCategoryRate";

    private static final String BASE_WEIGHT_PROP = "baseWeight";
    private static final String WEIGHT_PROP = "weight";
    private static final String MAX_RATE_PROP = "maxRate";
    private static final String ENABLED = "enabled";

    private final Map<MeasurerType, MeasurerSettings> measurerSettingsMap = new EnumMap<>(MeasurerType.class);
    private final Map<MeasurerType, Boolean> enabledMeasurersMap = new EnumMap<>(MeasurerType.class);

    @Autowired
    private Environment environment;

    @Getter
    @Value("${" + PROPERTY_PREFIX + G_MINIMAL_RATE_SUFFIX + "}")
    private int minimalRate;

    @Getter
    @Value("${" + PROPERTY_PREFIX + G_MAX_CATEGORY_RATE_SUFFIX + "}")
    private int maxCategoryRate;

    public Map<MeasurerType, MeasurerSettings> getMeasurerSettingsMap() {
        return new HashMap<>(measurerSettingsMap);
    }

    public Map<MeasurerType, Boolean> getEnabledMeasurersMap() {
        return new HashMap<>(enabledMeasurersMap);
    }

    @PostConstruct
    private void fillSettings() {

        for (MeasurerType measurerType : MeasurerType.values()) {

            int baseWeight = environment.getProperty(getPropertyFor(measurerType, BASE_WEIGHT_PROP), Integer.class);
            int weight = environment.getProperty(getPropertyFor(measurerType, WEIGHT_PROP), Integer.class);
            int maxRate = environment.getProperty(getPropertyFor(measurerType, MAX_RATE_PROP), Integer.class);
            boolean enabled = environment.getProperty(getPropertyFor(measurerType, ENABLED), Boolean.class, false);

            enabledMeasurersMap.put(measurerType, enabled);
            measurerSettingsMap.put(measurerType, new MeasurerSettings(baseWeight, weight, maxRate));
        }
    }

    private static String getPropertyFor(MeasurerType mt, String propName) {
        return format("%s%s.%s", PROPERTY_PREFIX, mt.getTypeName(), propName);
    }

}
