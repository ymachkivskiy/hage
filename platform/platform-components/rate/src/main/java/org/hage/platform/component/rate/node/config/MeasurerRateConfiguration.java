package org.hage.platform.component.rate.node.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class MeasurerRateConfiguration {
    @Getter
    private final int rateBaseWeight;
    @Getter
    private final int rateWeight;
    @Getter
    private final int maxRate;
}
