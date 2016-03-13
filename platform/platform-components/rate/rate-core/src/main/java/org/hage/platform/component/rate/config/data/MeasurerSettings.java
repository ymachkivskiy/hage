package org.hage.platform.component.rate.config.data;

import lombok.Data;

@Data
public class MeasurerSettings {
    private final int baseWeight;
    private final int weight;
    private final int maxRate;
}
