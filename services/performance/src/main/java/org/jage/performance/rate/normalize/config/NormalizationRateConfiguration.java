package org.jage.performance.rate.normalize.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jage.performance.node.MeasurerRateConfiguration;

@AllArgsConstructor
public class NormalizationRateConfiguration {
    @Getter
    private final GlobalRateConfiguration globalRateConfiguration;
    @Getter
    private final MeasurerRateConfiguration rateConfiguration;
}
