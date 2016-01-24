package org.hage.platform.rate.local.normalize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hage.platform.rate.model.RateCalculationSettings;

@AllArgsConstructor
public class NormalizationRateSettings {
    @Getter
    private final GlobalRateSettings globalRateSettings;
    @Getter
    private final RateCalculationSettings rateConfiguration;
}
