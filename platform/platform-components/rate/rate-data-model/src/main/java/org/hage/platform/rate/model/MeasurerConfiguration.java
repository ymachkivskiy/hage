package org.hage.platform.rate.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class MeasurerConfiguration implements Serializable {
    private final MeasurerType measurerType;
    private final RateCalculationSettings rateCalculationSettings;
}
