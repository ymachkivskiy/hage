package org.hage.platform.component.rate.config.data;

import lombok.Data;

@Data
public class NormalizationSettings {
    private final GlobalRateSettings globalRateSettings;
    private final MeasurerSettings measurerSettings;
}
