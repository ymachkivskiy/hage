package org.hage.platform.rate.local.config;

import lombok.Data;

@Data
public class NormalizationSettings {
    private final GlobalRateSettings globalRateSettings;
    private final MeasurerSettings measurerSettings;
}
