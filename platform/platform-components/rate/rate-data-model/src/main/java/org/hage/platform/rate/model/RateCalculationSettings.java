package org.hage.platform.rate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class RateCalculationSettings implements Serializable {
    private final int rateBaseWeight;
    private final int rateWeight;
    private final int maxRate;
}
