package org.hage.platform.rate.local.config;

import lombok.Data;

@Data
public class MeasurerSettings {
    private final int baseWeight;
    private final int weight;
    private final int maxRate;
}
