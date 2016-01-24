package org.hage.platform.rate.local.normalize;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
public class GlobalRateSettings {
    @Getter
    private final BigInteger maxGlobalRate;
}
