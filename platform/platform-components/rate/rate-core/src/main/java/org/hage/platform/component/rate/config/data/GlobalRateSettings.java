package org.hage.platform.component.rate.config.data;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GlobalRateSettings {
    private final BigInteger maxGlobalRate;
}
