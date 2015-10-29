package org.jage.performance.rate.normalize.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class GlobalRateConfig {
    @Getter
    private final BigInteger maxGlobalRate;
}
