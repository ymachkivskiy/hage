package org.jage.performance.node.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class GlobalRateConfiguration {
    @Getter
    private final BigInteger maxGlobalRate;
}
