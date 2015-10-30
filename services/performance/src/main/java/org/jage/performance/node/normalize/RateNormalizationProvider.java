package org.jage.performance.node.normalize;

import org.jage.performance.node.config.NormalizationRateConfiguration;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateConfiguration configuration);
}
