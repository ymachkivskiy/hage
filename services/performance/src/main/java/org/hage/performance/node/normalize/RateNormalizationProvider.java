package org.hage.performance.node.normalize;

import org.hage.performance.node.config.NormalizationRateConfiguration;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateConfiguration configuration);
}
