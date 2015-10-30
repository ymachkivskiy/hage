package org.jage.performance.node;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MeasurerRateConfiguration {
    @Getter
    private final int rateBaseWeight;
    @Getter
    private final int rateWeight;
    @Getter
    private final int maxRate;
}
