package org.jage.performance.rate.normalize;

import org.jage.performance.rate.normalize.config.RateConfiguration;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(RateConfiguration configuration);
}
