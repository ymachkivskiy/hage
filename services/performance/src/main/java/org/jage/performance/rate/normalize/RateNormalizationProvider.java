package org.jage.performance.rate.normalize;

import org.jage.performance.rate.normalize.config.NormalizationRateConfiguration;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateConfiguration configuration);
}
