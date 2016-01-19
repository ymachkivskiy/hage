package org.hage.platform.component.rate.node.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class GlobalRateConfiguration {
    @Getter
    private final BigInteger maxGlobalRate;
}
