package org.hage.platform.component.rate.node.normalize;

import org.hage.platform.component.rate.node.config.NormalizationRateConfiguration;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateConfiguration configuration);
}
