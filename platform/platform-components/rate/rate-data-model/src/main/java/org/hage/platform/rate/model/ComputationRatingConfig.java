package org.hage.platform.rate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Builder
@ToString
public class ComputationRatingConfig implements Serializable {

    @Getter
    private final Set<MeasurerType> enabledRateMeasureTypes;
    @Getter
    private final List<MeasurerRateConfig> measurerRateConfigs;

}
