package org.jage.performance.rate.normalize.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RateConfiguration {
    @Getter
    private final GlobalRateConfig globalRateConfig;
    @Getter
    private final CategoryRateConfiguration categoryConfiguration;
}
