package org.hage.platform.rate.local.config.data;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GlobalRateSettings {
    private final BigInteger maxGlobalRate;
}
