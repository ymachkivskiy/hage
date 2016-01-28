package org.hage.platform.rate.local.config.data;

import lombok.Data;

@Data
public class NormalizationSettings {
    private final GlobalRateSettings globalRateSettings;
    private final MeasurerSettings measurerSettings;
}
