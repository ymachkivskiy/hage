package org.hage.platform.node.rate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class MeasurerRateConfig implements Serializable {
    private final MeasurerType measurerType;
    private final int rateWeight;
    private final int maxRate;
}
