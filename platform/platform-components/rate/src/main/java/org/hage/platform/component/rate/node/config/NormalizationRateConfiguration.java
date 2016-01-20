package org.hage.platform.component.rate.node.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NormalizationRateConfiguration {
    @Getter
    private final GlobalRateConfiguration globalRateConfiguration;
    @Getter
    private final MeasurerRateConfiguration rateConfiguration;
}