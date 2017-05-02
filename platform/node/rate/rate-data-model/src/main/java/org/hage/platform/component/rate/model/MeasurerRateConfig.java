package org.hage.platform.component.rate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class MeasurerRateConfig implements Serializable {
    private final MeasurerType measurerType;
    private final int rateWeight;
    private final int maxRate;
}
