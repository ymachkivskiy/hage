package org.hage.platform.rate.local.config;

import lombok.Getter;
import org.hage.platform.rate.model.MeasurerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

import static java.lang.String.format;

@Component
public class Settings {

    private static final String PROPERTY_PREFIX = "hage.platform.rate.local.";

    private static final String MINIMAL_RATE_SUFFIX = "minimalRate";
    private static final String MAX_CATEGORY_RATE_SUFFIX = "maxCategoryRate";

    private static final String BASE_WEIGHT_SUFFIX = "baseWeight";
    private static final String WEIGHT_SUFFIX = "weight";
    private static final String MAX_RATE_SUFFIX = "maxRate";

    private final Map<MeasurerType, MeasurerSettings> measurerSettingsMap = new EnumMap<>(MeasurerType.class);

    @Autowired
    private Environment environment;

    @Getter
    @Value("${" + PROPERTY_PREFIX + MINIMAL_RATE_SUFFIX + "}")
    private int minimalRate;

    @Getter
    @Value("${" + PROPERTY_PREFIX + MAX_CATEGORY_RATE_SUFFIX + "}")
    private int maxCategoryRate;

    public MeasurerSettings settingsFor(MeasurerType measurerType) {
        return measurerSettingsMap.get(measurerType);
    }

    @PostConstruct
    private void fillSettings() {
        for (MeasurerType mt : MeasurerType.values()) {

            int baseWeight = environment.getProperty(format("%s%s.%s", PROPERTY_PREFIX, mt.getTypeName(), BASE_WEIGHT_SUFFIX), Integer.class);
            int weight = environment.getProperty(format("%s%s.%s", PROPERTY_PREFIX, mt.getTypeName(), WEIGHT_SUFFIX), Integer.class);
            int maxRate = environment.getProperty(format("%s%s.%s", PROPERTY_PREFIX, mt.getTypeName(), MAX_RATE_SUFFIX), Integer.class);

            measurerSettingsMap.put(mt, new MeasurerSettings(baseWeight, weight, maxRate));
        }
        boolean dupa = false;
    }

}
